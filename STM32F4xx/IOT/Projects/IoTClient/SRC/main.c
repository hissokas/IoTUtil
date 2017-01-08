#include "LIB_Config.h"
#include "Hal_rgb_led.h"
#include "Hal_uart.h"
#include "EMSP_API.h"
#include "em380c_hal.h"

#include "hal_infrared.h"
#include "hal_temp_hum.h"
#include "Hal_motor.h"
#include "Hal_led.h"

#include <stdio.h>
#include <string.h>
#include <stdlib.h>	

#define	STARTFLAG 0xAAAAAAAB

extern int buff_pos;
extern bool bInfrared;

typedef struct {
	unsigned short ID;
	unsigned short V1; //传感器数值1
	unsigned short V2; //传感器数值2
	unsigned short V3; //传感器数值2
}__attribute__((packed)) sensor_data_frame;

typedef struct {
	unsigned char 	START_F0;
	unsigned char 	START_F1;
	unsigned char 	START_F2;
	unsigned char 	START_F3;
	unsigned char ID;
	unsigned char	sensor_num; //Senors
	unsigned short	heartbeat; //心跳包
	
	sensor_data_frame	sensor_dev1; //Send out Hum&Temp
	sensor_data_frame	sensor_dev2; //Send out RGB
	sensor_data_frame	sensor_dev3; //Send out IR Status
	
	unsigned char chkorl;
	
}__attribute__((packed)) device_comm_frame;

typedef struct {
	short v1;
	short v2;
	short v3;
}__attribute__((packed)) receive_Sensor_data;

typedef struct {
	unsigned char 	START_F0;
	unsigned char 	START_F1;
	unsigned char 	START_F2;
	unsigned char 	START_F3;
	
	receive_Sensor_data sensor_dev1; //LED
	receive_Sensor_data sensor_dev2; //RGB
	receive_Sensor_data sensor_dev3; //Motor
	
	unsigned char chkorl;
}__attribute__((packed)) receive_IOT;

char const SERVER_IP[] = { "192.168.253.6" }; /* 目标IP地址 */
char const GATEWAY_IP[] = { "192.168.253.1" };
char const WIFI_SSID[] = { "Kirk-AP" };
char const WIFI_KEY[] = { "87394178" };
uint16_t SERVER_PORT = 23344;

char const LOCAL_IP[] = { "10.66.40.0" }; /* 本地IP地址 */

u8 rcv_buf_from_server[256];
receive_Sensor_data sensor_data[10];

/* Private variables ---------------------------------------------------------*/
EM380C_parm_TypeDef parm;
u32 version;

void setup_wifi(void)
{
	EMW3612_CMD;
	delay_ms(200);
	
	LED_RGB_Control(0, 0, 10);
	delay_ms(100);
	EM380C_Init(BaudRate_115200, WordLength_8b, StopBits_1, Parity_No,
			HardwareFlowControl_None, buffer_128bytes); //if you dont know EM380C's baudrate,you should use	EM380C_scan_Init(...)
	delay_ms(200);

	while (EM380C_Get_ver(&version) == EM380ERROR)
		;
//		ret = EM380C_Get_ver(&version);
	LED_RGB_Control(10, 0, 0);
	delay_ms(500);

	parm.wifi_mode = STATION;
	strcpy((char*) parm.wifi_ssid, WIFI_SSID);
	strcpy((char*) parm.wifi_wepkey, WIFI_KEY);
	parm.wifi_wepkeylen = strlen(WIFI_SSID);

	strcpy((char*) parm.local_ip_addr, LOCAL_IP);
	strcpy((char*) parm.remote_ip_addr, SERVER_IP);
	strcpy((char*) parm.gateway, GATEWAY_IP);
	strcpy((char*) parm.net_mask, "255.255.255.0");
	parm.portH = SERVER_PORT >> 8;
	parm.portL = SERVER_PORT & 0xff;
	parm.connect_mode = TCP_Client;
	parm.use_dhcp = DHCP_Enable;
	parm.use_udp = TCP_mode;
	parm.UART_buadrate = BaudRate_115200;
	parm.DMA_buffersize = buffer_64bytes;
	parm.use_CTS_RTS = HardwareFlowControl_None;
	parm.parity = Parity_No;
	parm.data_length = WordLength_8b;
	parm.stop_bits = StopBits_1;

	parm.io1 = None;
	strcpy((char*) parm.wpa_key, WIFI_KEY);
	parm.security_mode = Auto;

	while (EM380C_Set_Config(&parm) == EM380ERROR)
		;									 //EMSP API, set EM380C status
	delay_ms(200);
	LED_RGB_Control(0, 10, 0);

	while (EM380C_Startup() == EM380ERROR)
		;
	delay_ms(200);
	LED_RGB_Control(5, 5, 5);

//	EM380C_Reset();	
	delay_ms(200);	
	EMW3612_DAT;
	delay_ms(200);
}

int check(char *bytes){
	char re=0x00;
	char *t = bytes;
	int i;
	for (i=0;i<46;i++){
			re^=bytes[i];
		}
	return re!=bytes[i];
}

int process_rcv_data(){
	int datalen;
	vs8 ret = -1;
	receive_IOT *prcv_buf = (receive_IOT *) rcv_buf_from_server;
	
	datalen = UART_receive_buf(rcv_buf_from_server);
	buff_pos=0;
	
	if (!((rcv_buf_from_server[0] == 0xaa) &&
		(rcv_buf_from_server[1] == 0xaa) &&
		(rcv_buf_from_server[2] == 0xaa) &&
		(rcv_buf_from_server[3] == 0xab))){
		ret = -1;
		goto done;
	}
	
	/*if (check(rcv_buf_from_server)) {	// check sum error
		ret = -1;
		goto done;
	}*/
	
	// data ok
	memcpy(sensor_data, (u8 *)&prcv_buf->sensor_dev1, sizeof(receive_Sensor_data)*3);
	ret = 0;
done:
	return ret;
}

void setCheckCode(device_comm_frame *data){
	char *t;
	char CheckCode = 0x00;
	int i;
	t = &( (data->START_F0));
	
	for (i=0;i<49;i++){
		CheckCode ^=*t;
		t++;
	}
	*t = CheckCode;
}


void Hal_Init(void)
{
	UARTx_Init();
	RGB_LED_Init();
	IR_Init();
	DHT11_Init();
	LED_GPIO_Init();
	Motor_Init();
	Motor_status(0);
}

int main(void)
{
	int cnt = 1;
	device_comm_frame tcpip_data;
	short R = 0, G = 0, B = 0;
	short v1,v2;
	
	system_init();
	Hal_Init();
	setup_wifi();

	/*给Header赋值*/
	tcpip_data.START_F0 = (STARTFLAG >> 24) & 0xff;
	tcpip_data.START_F1 = (STARTFLAG >> 16) & 0xff;
	tcpip_data.START_F2 = (STARTFLAG >> 8) & 0xff;
	tcpip_data.START_F3 = (STARTFLAG >> 0) & 0xff;
	tcpip_data.heartbeat = 0;
	tcpip_data.ID=89;
	tcpip_data.sensor_num=3;
	
	while (cnt++) {
		tcpip_data.heartbeat++;
		enable_irq();
		DHT11_Read_Data((u8*)&v1,(u8*)&v2);
	
		// Read Humidity and Temperature
		tcpip_data.sensor_dev1.ID = 1;
		tcpip_data.sensor_dev1.V1 = v1; //T
		tcpip_data.sensor_dev1.V2 = v2; //H
		
		// RGB
		tcpip_data.sensor_dev2.ID = 2;
		tcpip_data.sensor_dev2.V1 = R;
		tcpip_data.sensor_dev2.V2 = G;
		tcpip_data.sensor_dev2.V3 = B;
		
		//IR
		IR_Handle();
		tcpip_data.sensor_dev3.ID = 3;
		if(bInfrared){
			tcpip_data.sensor_dev3.V1=1;
		}else{
			tcpip_data.sensor_dev3.V1=0;
		}
		
		//最后计算校验码
		setCheckCode(&tcpip_data);
		UART_send_buf((uint8_t *)&tcpip_data,sizeof(device_comm_frame));
		delay_ms(500);
		disable_irq();

		if (process_rcv_data() == 0){	//OK	
			if(sensor_data[0].v1){
				LED_ON(LED2);
			}
			else{
				LED_OFF(LED2);
			}
			
			R = sensor_data[1].v1;
			G = sensor_data[1].v2;
			B = sensor_data[1].v3;
			LED_RGB_Control(R,G,B);
			Motor_status(sensor_data[2].v1);
			
		}
		
		
		/*switch (cnt % 3) {
		case 0:
			LED_RGB_Control(10, 0, 0);
			break;
		case 1:
			LED_RGB_Control(0, 10, 0);
			break;
		case 2:
			LED_RGB_Control(0, 0, 10);
			break;
		default:
			break;
		}*/
	}
}

/*-------------------------------END OF FILE-------------------------------*/

