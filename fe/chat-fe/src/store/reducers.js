import { combineReducers } from 'redux'
import {  SET_USER, SET_WEBSOCKET, SET_CHATLISTSMAP, SET_LEFTITEMLIST, SET_CHAT, REV_CHAT, SET_ONLINELIST, } from './actions'



/**
 * 用户信息
 * @param {*} state 
 * @param {*} action 
 */
function user(state = {}, action) {
    switch (action.type) {
        case SET_USER: {
            return action.user
        }
        default:
            return state
    }
}

/**
 * chatlistsMap
 * @param {} state 
 * @param {*} action 
 */
function chatListsMap(state=new Map() ,action) {
    switch (action.type) {
        case SET_CHATLISTSMAP : {
            return action.chatListsMap
        }
        case SET_CHAT: {
            const chat = action.chat
            const key = `${chat.type}_${chat.id}`
            const chatList = state.get(key) || []
            const newArr = [...chatList, chat]
            state.set(key, newArr)
            const newMap = new Map(state)
            return newMap
        }
        case REV_CHAT: {
            const chat = action.chat
            const key = `${chat.type}_${chat.type === "2"? chat.id : chat.userId}`
            const chatList = state.get(key) || []
            const newArr = [...chatList, chat]
            state.set(key, newArr)
            const newMap = new Map(state)
            return newMap
        }
        default:
            return state
    }
}

function leftItemList(state=[] ,action) {
    switch (action.type) {
        case SET_LEFTITEMLIST : {
            return action.leftItemList
        }
        default:
            return state
    }
}
/**
 * websocket对象
 * @param {*} state 
 * @param {*} action 
 */
function websocket(state = null, action) {
    switch (action.type) {
        case SET_WEBSOCKET: {
            return action.websocket
        }
        default:
            return state
    }
}

/**
 * 在线列表
 * @param {*} state 
 * @param {*} action 
 */
function onlineList(state = [], action) {
    switch (action.type) {
        case SET_ONLINELIST: {
            return action.onlineList
        }
        default:
            return state
    }
}


const rootReducer = combineReducers({
    user,
    websocket,
    chatListsMap,
    leftItemList,
    onlineList,
})

export default rootReducer 