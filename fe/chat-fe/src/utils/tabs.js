import React from 'react'
import LoadableComponent from './LoadableComponent'
//const Test = React.lazy(() => import('./Test'));   //报错，就没用React.lazy了
const Chat = LoadableComponent(import('../pages/Chat'), true);
const Blog = LoadableComponent(import('../pages/Blog'), true)

const menu = [
    {
        name: '聊天',
        icon: 'wechat',
        key: 'Chat'
    },
    {
        name: '动态圈',
        icon: 'global',
        key: 'Blog'
    },
    {
        name: '通讯录',
        icon: "solution",
        key: 'friends'
    },
    {
        name: '红包详情',
        icon: "transaction",
        key: 'RedBacket'
    },
    {
        name: '关于',
        icon: 'question-circle',
        key: 'About'
    }
]

const tabs = {
 
   
    Chat: <Chat />,
    Blog: <Blog />
}

export {
    menu,
    tabs
}