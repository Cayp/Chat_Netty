import React from 'react'
import { notification, message, Avatar, } from 'antd'
import { myAxios } from '../utils/myAxios'
import { handleChatListToMap, getUser, replaceImg } from '../utils/util'



// 虽然用户信息放在localStorage也可以全局使用，但是如果放在localStorage中，用户信息改变时页面不会实时更新
export const SET_USER = 'SET_USER'
export function setUser(user) {
    return {
        type: SET_USER,
        user
    }
}
//聊天对象list
export const SET_LEFTITEMLIST = 'SET_LEFTITEMLIST'
export function setLeftItemList(leftItemList) {
    return {
        type: SET_LEFTITEMLIST,
        leftItemList
    }
}
/**
 * 初始化聊天对象list
 */
export function initLeftItemList(userId) {
    return async function (dispatch) {
        myAxios.get(`/chat/relation/leftItemList?userId=${userId}`)
               .then((response) => {
                   let json = response.data;
                   if (json.code == 20000) {
                       dispatch(setLeftItemList(json.dataList))
                   } else {
                       message.error(json.message)
                   }
           })
    }
}

//聊天对象与聊天记录的对应的map
export const SET_CHATLISTSMAP = 'SET_CHATLISTSMAP'
export function setChatListsMap(chatListsMap) {
     return {
         type: SET_CHATLISTSMAP,
         chatListsMap
     }
}

/**
 * 初始化聊天对象与聊天记录的对应的map
 * @param {*} userId 
 */
export function initChatListsMap(userId) {
    return async function (dispatch) {
        myAxios.get(`/chat/relation/unReadChatList?userId=${userId}`)
               .then((response) => {
                   let json = response.data;
                   if (json.code == 20000) {
                    let chatListsMap = handleChatListToMap(json.dataList)   
                    dispatch(setChatListsMap(chatListsMap))
                   } else {
                    message.error(json.message)
                   }
               })
    }
}


//设置websocket对象
export const SET_WEBSOCKET = 'SET_WEBSOCKET'
export function setWebsocket(websocket) {
    return {
        type: SET_WEBSOCKET,
        websocket
    }
}

/**
 * 添加聊天信息
 */
export const SET_CHAT = 'SET_CHAT'
export function setChat(chat) {
    return {
        type: SET_CHAT,
        chat
    }
}

export const REV_CHAT = 'REV_CHAT'
export function revChat(chat) {
    return {
        type: REV_CHAT,
        chat
    }
}


export function addChat(chat) {
    return async function (dispatch) {
        dispatch(setChat(chat))
    }
}

export const SET_ONLINELIST = 'SET_ONLINELIST'   //设置在线列表
export function setOnlinelist(onlineList) {
    return {
        type: SET_ONLINELIST,
        onlineList
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
            heartCheck.reset();
            //接收到服务端发回的心跳
            if (event.data == "pong"){
                return;
            }
            const data = JSON.parse(event.data)
            const user = getUser() 
            console.log(data)
            if (data.type === "1") {
                dispatch(revChat(data))
                notification.open({
                    message: data.name,
                    description: <div style={{wordBreak:'break-all'}} dangerouslySetInnerHTML={{ __html: replaceImg(data.content) }} />,
                    icon: <Avatar src={`/chat/images/avatar/${data.avatar}`} />
                })
            } else if (data.type === "2") {
                if (data.userId !== user.id) {
                dispatch(revChat(data))   
                notification.open({
                    message: "聊天广场",
                    description:
                            <div>
                                <div className='username'>{data.name} :</div>
                                <div className='chat-content' dangerouslySetInnerHTML={{ __html: replaceImg(data.content) }} />
                            </div>
                       ,
                    icon: <Avatar src={`/chat/images/avatar/${data.avatar}`}/>
                })
               }
            } else if (data.type === "6" || data.type === "9") {
                dispatch(setOnlinelist(JSON.parse(data.onlineList)))
                notification.info({
                    message: '提示',
                    description: data.msg,
                    icon: <Avatar src={'/chat/images/avatar/group.jpg'}/>
                })
            }
        
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
                    websocket.send("ping");
                    self.serverTimeoutObj = setTimeout(function () {
                        websocket.close(); //如果onclose会执行reconnect，我们执行ws.close()就行了.如果直接执行reconnect 会触发onclose导致重连两次
                    }, self.timeout)
                }, this.timeout)
            }
    }
    dispatch(setWebsocket(websocket))
  }
}
