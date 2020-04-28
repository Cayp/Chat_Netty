import React from 'react'
import LoadableComponent from './LoadableComponent'
//const Test = React.lazy(() => import('./Test'));   //报错，就没用React.lazy了
const Chat = LoadableComponent(import('../pages/Chat'), true);


const menu = [
    {
        name: '聊天',
        icon: 'qq',
        key: 'Chat'
    },
    {
        name: '朋友动态',
        icon: 'message',
        key: 'MessageBoard'
    },
    {
        name: '通讯录',
        icon: "message",
        key: 'friends'
    },
    {
        name: '红包详情',
        icon: "message",
        key: 'RedBacket'
    },
    {
        name: '关于',
        icon: 'info-circle',
        key: 'About'
    }
]

const tabs = {
 
   
    Chat: <Chat />
}

export {
    menu,
    tabs
}