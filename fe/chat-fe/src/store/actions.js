import React from 'react'
import { notification, message } from 'antd'


//设置websocket对象
export const SET_WEBSOCKET = 'SET_WEBSOCKET'
export function setWebsocket(websocket) {
    return {
        type: SET_WEBSOCKET,
        websocket
    }
}

export function initWebSocket(userId) {
    return async function (dispatch) {
        const websocket = new WebSocket(`ws://localhost:10000/webSocket/${userId}`)
         //建立连接时触发
         websocket.onopen = function () {
            message.success("连接成功")
            heartCheck.start();
        }
        //监听服务端的消息事件
        websocket.onmessage = function (event) {
           // const data = JSON.parse(event.data)
            console.log(event.data)
            heartCheck.reset();
            //接收到服务端发回的心跳
            
        }
        websocket.onclose = function () {

        } 
        //监听窗口关闭事件，当窗口关闭时，主动去关闭websocket连接，防止连接还没断开就关闭窗口，server端会抛异常。
        window.onbeforeunload = function () {
            websocket.close();
        };
        //心跳检测,每10s心跳一次
        var heartCheck = {
            timeout: 10000,
            timeoutObj: null,
            serverTimeoutObj: null,
            reset: function () {
                clearTimeout(this.timeoutObj);
                clearTimeout(this.serverTimeoutObj);
                this.start();
            },
            start: function () {
                var self = this;
                this.timeoutObj = setTimeout(function () {
                    console.log("test")
                    websocket.send("ping");
                    self.serverTimeoutObj = setTimeout(function () {
                        websocket.close(); //如果onclose会执行reconnect，我们执行ws.close()就行了.如果直接执行reconnect 会触发onclose导致重连两次
                    }, self.timeout)
                }, this.timeout)
            }
    }
  }
}
