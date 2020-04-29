import { combineReducers } from 'redux'
import { SET_WEBSOCKET, SET_CHATLISTSMAP, SET_LEFTITEMLIST, ADD_CHAT } from './actions'


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
        case ADD_CHAT: {
            let chat = action.chat
            let key = `${chat.type}_${chat.fromId}`
            let chatList = state.get(key)
            state.set(key, [...chatList, action])
            return state
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

const rootReducer = combineReducers({
    websocket,
    chatListsMap,
    leftItemList
})

export default rootReducer 