/*
 * @Description: In User Settings Edit
 * @Author: your name
 * @Date: 2019-07-07 11:03:21
 * @LastEditTime: 2019-08-25 16:05:10
 * @LastEditors: Please set LastEditors
 */
import React, { Component } from 'react';
import { message, Avatar, Divider, List as AntdList, Layout } from 'antd'
import {  initWebSocket, addChat } from '../store/actions'
import { connect, } from 'react-redux'
import { bindActionCreators } from 'redux'
import { replaceImg, throttle, getUser } from '../utils/util'
import moment from 'moment'
import { ContentUtils } from 'braft-utils'
import BraftEditor from 'braft-editor'
import { myAxios } from '../utils/myAxios'
import { List, CellMeasurer, CellMeasurerCache, } from 'react-virtualized'
import 'braft-editor/dist/index.css'
import './style.less'

const store = connect(
    (state) => ({ websocket: state.websocket, leftItemList: state.leftItemList, chatListsMap: state.chatListsMap, onlineList: state.onlineList}),
    (dispatch) => bindActionCreators({ initWebSocket, addChat }, dispatch)
)

const cache = new CellMeasurerCache({
    defaultHeight: 96,
    fixedWidth: true
});


@store
class Chat extends Component {
    state = {
        editorState: BraftEditor.createEditorState(null),
        userList: [], //所有用户列表
        selectLeftItem: '2_1',
    }
    componentDidMount() {
        this.scrollToRow()
        this.getUserList()
        window.onmouseup = this.onMouseUp
    }
    //首次渲染不会执行此方法
    componentDidUpdate(prevProps) {
        if (this.props.chatListsMap !== prevProps.chatListsMap) {
            this.scrollToRow()
        }
        if (this.props.onlineList !== prevProps.onlineList) {
            this.handleUserList()
        }
    }
    componentWillUnmount() {
        window.onmouseup = null
    }

    chatLeftItems = () => {
        let { selectLeftItem} = this.state;
        const leftItemList = this.props.leftItemList;
        const chatListsMap = this.props.chatListsMap; 
        return (
            <AntdList
               dataSource={leftItemList}
               style={{ overflowY: "scroll", height: "100%"}}
               renderItem={item => {
                   const key = `${item.type}_${item.id}`
                   const chatList = chatListsMap.get(key) || []
                   const lastChat = chatList[chatList.length - 1] || {}
                   return (<AntdList.Item style={{padding: "0px"}}>
               <div className='left-item' 
                    style={{background: key === selectLeftItem ? '#f3f3f3' : ''}}
                    onClick={() =>this.setState({ selectLeftItem: key })}>
                            <div><Avatar size='large' src={`/chat/images/avatar/${item.avatar}`} /></div>
                            <div className='left-item-text'>
                                <div className='group-name'>
                                    <span>{item.name}</span>
                                    <span>{this.handleTime(lastChat.time, true).split(' ')[0]}</span>
                                </div>
                                <div className='group-message' style={{ display: lastChat.id ? 'flex' : 'none' }}>
                                    <div style={{ flexFlow: 1, flexShrink: 0,  display: lastChat.type && lastChat.type === "2" ? 'flex' : 'none'}}>{lastChat.name}:&nbsp;</div>
                                    <div className='ellipsis' dangerouslySetInnerHTML={{ __html: replaceImg(lastChat.content) }} />
                                </div>
                            </div>
                        </div>
           </AntdList.Item>)}
              }
           />
        )
    }

    scrollToRow = () => {
        // 页面首次进入时并没有滚动到最底部，用下面这种方法进行处理
        const { selectLeftItem } = this.state 
        const chatList = this.props.chatListsMap.get(selectLeftItem) || []
        const rowIndex = chatList.length - 1
        this.chatListDom.scrollToRow(rowIndex)
        clearTimeout(this.scrollToRowTimer)
        this.scrollToRowTimer = setTimeout(() => {
            if (this.chatListDom) {
                this.chatListDom.scrollToRow(rowIndex)
            }
        }, 10)
    }
    /**
     * 获取所有用户列表
     */
    getUserList = async () => {
        const res = await myAxios.get('/chat/user/users')
        await this.setState({
            userList: res.data.dataList || []
        })
        this.handleUserList()       
    }

    /**
     * 处理用户列表(管理员、在线用户放在数组前面)
     */
    handleUserList = () => {
        const userList = this.state.userList
        const onlineList = this.props.onlineList
        let list2 = []
        let list3 = []
        for (let item of userList) {
            const isHave = onlineList.find(i => i.id === item.id)
            const user = {
                ...item,
                online: !!isHave
            }
           if (!!isHave) {
                list2.push(user)
            } else {
                list3.push(user)
            }
        }
        this.setState({
            userList: list2.concat(list3)
        })
    }
    handleEditorChange = (editorState) => {
        this.setState({ editorState })
    }
    //定制键盘命令
    handleKeyCommand = (command) => {
        //如果是回车命名就发送信息
        if (command === 'split-block') {
            this.onSend()
            return 'handled';
        }
        return 'not-handled';
    }
    onSend = () => {
        const editorState = this.state.editorState
        const selectLeftItemArr = this.state.selectLeftItem.split("_")
        const htmlContent = editorState.toHTML()
        const user = getUser()
        if (editorState.isEmpty()) {
            return message.warning('请先输入聊天内容')
        }
        const chatMessage = {
            type: selectLeftItemArr[0],
            id: selectLeftItemArr[1],
            userId: user.id,
            name: user.name,
            avatar: user.avatar || "1",
            content: htmlContent
        }
        const websocket = this.props.websocket
        if (websocket.readyState !== 1) {
            //断开连接，重新连接
            this.props.initWebSocket(this.props.user)
            return message.warning('消息发送失败，请重新发送')
        }
        websocket.send(JSON.stringify(chatMessage))
        this.props.addChat(chatMessage)
        this.setState({
            editorState: ContentUtils.clear(this.state.editorState)
            // editorState: BraftEditor.createEditorState(null)    //用这种方法清空富文本编辑器，在下次输入时光标容易跳动
        })
    }
    myUploadFn = async (param) => {
        const formData = new FormData();
        formData.append('picture', param.file);
        let config = {
            headers: {
                'Content-Type': 'multipart/form-data'
            }
        }
        myAxios.put('/chat/picture/chat', formData, config)
               .then((response) => {
                   let json = response.data
                   if (json.code == 20000) {
                    param.success(json.data)
                   } else {
                    param.error({
                        msg: '上传错误'
                    })
                }
            })
    }
    onMouseDown = (e) => {
        e.persist()
        e.preventDefault()
        this.isDown = true
        this.chatHeader.style.cursor = 'move'
        //保存初始位置
        this.mouse = {
            startX: e.clientX,
            startY: e.clientY,
            offsetLeft: this.chatBox.offsetLeft,
            offsetTop: this.chatBox.offsetTop,
        }
    }
    //节流函数优化
    onMouseMove = throttle((e) => {
        if (!this.isDown) {
            return
        }
        //计算偏移位置
        let offsetLeft = this.mouse.offsetLeft + e.clientX - this.mouse.startX
        let offsetTop = this.mouse.offsetTop + e.clientY - this.mouse.startY

        //设置偏移距离的范围[0,this.chatContainer.clientWidth - 780]
        offsetLeft = Math.max(0, Math.min(offsetLeft, this.chatContainer.clientWidth - 780))
        offsetTop = Math.max(0, Math.min(offsetTop, window.innerHeight - 624))

        this.chatBox.style.left = offsetLeft + 'px';
        this.chatBox.style.top = offsetTop + 'px';
    }, 10)
    onMouseUp = () => {
        this.isDown = false
        this.chatHeader.style.cursor = 'default'
        this.mouse = null
    }
    //处理时间
    handleTime = (time1, small) => {
        if (!time1) {
            return ''
        }
        const time = time1 * 1000
        const HHmm = moment(time).format('HH:mm')
        //不在同一年，就算时间差一秒都要显示完整时间
        if (moment().format('YYYY') !== moment(time).format('YYYY')) {
            return moment(time).format('YYYY-MM-DD HH:mm:ss')
        }
        //判断时间是否在同一天
        if (moment().format('YYYY-MM-DD') === moment(time).format('YYYY-MM-DD')) {
            return HHmm
        }
        //判断时间是否是昨天。不在同一天又相差不超过24小时就是昨天
        if (moment().diff(time, 'days') === 0) {
            return `昨天 ${HHmm}`
        }
        //判断时间是否相隔一周
        if (moment().diff(time, 'days') < 7) {
            const weeks = ['一', '二', '三', '四', '五', '六', '日']
            return `星期${weeks[moment(time).weekday()]} ${HHmm}`
        }
        if (small) {
            return moment(time).format('MM-DD HH:mm')
        } else {
            return moment(time).format('M月D日 HH:mm')
        }

    }
    render() {
        const { editorState, userList, selectLeftItem } = this.state
        const chatListsMap = this.props.chatListsMap
        const chatList = chatListsMap.get(selectLeftItem) || [];
        const user = getUser()
        const onlineList = this.props.onlineList
        const controls = ['emoji', 'italic', 'text-color', 'separator', 'link', 'separator', 'media']
        // 禁止上传video、audio
        const media = {
            uploadFn: this.myUploadFn,
            accepts: {
                image: 'image/png,image/jpeg,image/gif,image/webp,image/apng,image/svg',
                video: false,
                audio: false
            },
            externals: {
                image: 'image/png,image/jpeg,image/gif,image/webp,image/apng,image/svg',
                video: false,
                audio: false,
                embed: false
            }
        }
        const hooks = {
            'toggle-link': ({ href, target }) => {
                const pattern = /^((ht|f)tps?):\/\/([\w-]+(\.[\w-]+)*\/?)+(\?([\w\-.,@?^=%&:/~+#]*)+)?$/
                if (pattern.test(href)) {
                    return { href, target }
                }
                message.warning('请输入正确的网址')
                return false
            }
        }
        return (
            <div className='chat-container' ref={el => this.chatContainer = el}>
                <div className='chat-box' ref={el => this.chatBox = el}>
                    <div className='chat-header' onMouseDown={this.onMouseDown} onMouseMove={this.onMouseMove} ref={el => this.chatHeader = el}>
                    </div>
                    <div className='chat-body'>
                        <div className='left'>
                            {this.chatLeftItems()}
                        </div>
                        <div className='main'>
                            <List
                                ref={el => this.chatListDom = el}
                                width={443}
                                height={328}
                                style={{ outline: 'none' }}
                                rowCount={chatList.length}
                                deferredMeasurementCache={cache}
                                rowHeight={cache.rowHeight}
                                rowRenderer={({ index, isScrolling, key, parent, style }) => (
                                    <CellMeasurer
                                        cache={cache}
                                        columnIndex={0}
                                        key={key}
                                        parent={parent}
                                        rowIndex={index}
                                    >
                                        <div style={style} className='chat-item'>
                                            {/* 两条消息记录间隔超过3分钟就显示时间 */}
                                            {(index === 0 || chatList[index].time - chatList[index - 1].time > 3 * 60 * 1000) && (
                                                <div className='time'>{this.handleTime(chatList[index].time)}</div>
                                            )}
                                            <div className={`chat-item-info ${user.id === chatList[index].userId ? 'chat-right' : ''}`}>
                                                <div><Avatar src={`/chat/images/avatar/${chatList[index].avatar}`} /></div>
                                                <div className='chat-main'>
                                                    <div className='username'>{chatList[index].name}</div>
                                                    <div className='chat-content' dangerouslySetInnerHTML={{ __html: chatList[index].content }} />
                                                </div>
                                            </div>

                                        </div>
                                    </CellMeasurer>
                                )}
                            />
                            <div className='chat-editor-wrapper'>
                                <BraftEditor
                                    draftProps={{
                                        handleKeyCommand: this.handleKeyCommand
                                    }}
                                    media={media}
                                    hooks={hooks}
                                    value={editorState}
                                    onChange={this.handleEditorChange}
                                    contentStyle={styles.contentStyle}
                                    controlBarStyle={styles.controlBarStyle}
                                    controls={controls}
                                />
                            </div>
                        </div>
                        <div className='right' style={{ display: selectLeftItem.split("_")[0] === "2" ? 'flex' : 'none'}}>
                            <div style={{ height: 162 }}>
                                <div style={{ padding: 5 }}>群公告</div>
                                <div style={styles.center}>
                                    <img src={require('./imgs/zanwu.png')} alt="" style={{ width: '80%' }} />
                                </div>
                                <Divider style={{ margin: '10px 0 0' }} />
                                <div className='member'>成员 {onlineList.length}/{userList.length}</div>
                            </div>
                            <List
                                width={134}
                                height={296}
                                style={{ outline: 'none' }}
                                rowCount={userList.length}
                                rowHeight={35}
                                rowRenderer={({ key, index, style, }) => (
                                    <div key={key} className='user-item' style={style}>
                                        <div className={`avatar-box ${userList[index].online ? '' : 'mask'}`}>
                                            <img style={{ width: '100%', height: '100%' }} src={`/chat/images/avatar/${userList[index].avatar}`} alt="" />
                                            <div />
                                        </div>
                                        <div className='ellipsis' style={{ flexGrow: 1, margin: '0 3px 0 5px' }}>{userList[index].name}</div>
                                    </div>
                                )}
                            />
                        </div>
                    </div>
                </div>
            </div>
        );
    }
}

const styles = {
    contentStyle: {
        height: 100,
        paddingBottom: 0,
        transform: 'translateY(-15px)',
        fontSize: 14
    },
    controlBarStyle: {
        boxShadow: 'none'
    },
    center: {
        display: 'flex',
        justifyContent: 'center',
        alignItems: 'center'
    }
}

export default Chat;