import React from 'react'
import LoadableComponent from './LoadableComponent'
//const Test = React.lazy(() => import('./Test'));   //报错，就没用React.lazy了
const Chat = LoadableComponent(import('../pages/Chat'), true);
const Blog = LoadableComponent(import('../pages/Blog'), true)
const About = LoadableComponent(import('../pages/About'), true);
const RedBacketSquare = LoadableComponent(import('../pages/RedPacketSquare'), true);
const Wallet = LoadableComponent(import('../pages/Wallet'), true);
const Contacts = LoadableComponent(import('../pages/Contacts'), true);
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
        key: 'Contacts'
    },
    {
        name: '红包广场',
        icon: "transaction",
        key: 'RedBacketSquare'
    },
    {
        name: '钱包',
        icon: "wallet",
        key: 'Wallet'
    },
    {
        name: '关于',
        icon: 'question-circle',
        key: 'About'
    }
]

const tabs = {
 
   
    Chat: <Chat />,
    Blog: <Blog />,
    Contacts: <Contacts />,
    RedBacketSquare: <RedBacketSquare />,
    Wallet: <Wallet />,
    About: <About />
}

export {
    menu,
    tabs
}