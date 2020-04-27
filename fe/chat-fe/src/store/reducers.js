import { combineReducers } from 'redux'
import { SET_WEBSOCKET } from './actions'

/**
 * websocket对象
 * @param {*} state 
 * @param {*} action 
 */
function webSocket(state = null, action) {
    switch (action.type) {
        case SET_WEBSOCKET: {
            return action.webSocket
        }
        default:
            return state
    }
}

const rootReducer = combineReducers({
    webSocket
})

export default rootReducer 