import React from 'react';
import { Row, Col, Input, Button, Divider, Avatar, Table, message, Modal, List } from 'antd';
import qs from 'qs'
import { myAxios } from '../utils/myAxios';
import { REQUEST_AGREE, REQUEST_DISAGREE } from "../utils/util";
import moment from 'moment'
const { Search } = Input;

class Contacts extends React.Component {


    constructor(props) {
        super(props);
        this.state = {
            friends: [],
            searchList: [],
            requestList: [],
            searchVisible: false,
            requestVisible: false,
        }
    }

    componentDidMount() {
        this.getFriends();
    }

    getFriends = () => {
        myAxios.get('/chat/user/getFriends')
               .then((response) => {
                let json = response.data
                this.setState({
                    friends: json.dataList
                })
               })
    }

    deleteFriend = (friendId) => {
        Modal.confirm({
            title: '提示',
            content: `确认删除该好友吗？`,
            onOk: async () => {
                myAxios.post('/chat/user/deleteFriend', qs.stringify({ friendId: friendId}))
                .then((response) => {
                    let json = response.data
                    if (json.code === 20000) {
                        message.success(json.message)
                        this.getFriends()
                    }
         })    
            },
        });
    }

    searchUser = (searchStr) => {
        myAxios.get(`/chat/user/search?searchString=${searchStr}`)
               .then((response) => {
                   let json = response.data
                   this.setState({
                       searchList: json.dataList
                   })
               })
    }

    getRequests = () => {
        myAxios.get('/chat/user/requests')
               .then((response) => {
                   let json = response.data
                   this.setState({
                       requestList: json.dataList
                   })
               })
    }

    addRequest = (toId) => {
        myAxios.post('/chat/user/request/add', { toId: toId})
               .then((response) => {
                   let json = response.data
                   if (json.code === 20000) {
                       message.success(json.message)
                   } else {
                       message.error(json.message)
                   }
               })
    }

    operateRequest = (requestId, type) => {
        this.deleteItemFromRequestList(requestId)
        myAxios.post('/chat/user/request/operate',  qs.stringify({ requestId: requestId, type: type }))
               .then((response) => {
                   let json = response.data
                   if (json.code === 20000) {
                       this.getFriends()
                       message.success(json.message)
                   } else {
                       message.error(json.message)
                   }
          })
    }

    deleteItemFromRequestList = (id) => {
        const requestList = this.state.requestList
        const newList = requestList.filter(item => item.id != id);
        this.setState({requestList: newList})
    }
    
    handleSearch = (value) => {
        if (value === '') {
            message.error("关键字不能为空!")
            return
        }
        this.setState({ searchVisible: true })
        this.searchUser(value)
    }

    handleCheckRequest = () => {
        this.setState({ requestVisible: true })
        this.getRequests()
    }

    handleBack = () => {
        this.setState({ searchVisible: false, searchList: [] })
    }

    handleRequestBack = () => {
        this.setState({ requestVisible: false, requestList: [] })
    }

    render() {
        const { friends, searchVisible, requestVisible, searchList, requestList } = this.state;
        const columns = [
            {
                title: '头像',
                dataIndex: 'icon',
                key: 'icon',
                render: icon => <Avatar src={`/chat/images/avatar/${icon}`}/>
            },
            {
                title: '名字',
                dataIndex: 'name',
                key: 'name',
                render: name => name
            },
            {
                title: '邮箱',
                dataIndex: 'mail',
                key: 'mail',
                render: mail => mail
            },
            {
                title: '操作',
                key: 'action',
                render: (text, record) => (
                    <Button type="danger" onClick={() => this.deleteFriend(record.account)}>删除</Button>
                )
            }
        ]
        return (
            <div style={{ padding: 30}}>
                <Row>
                    <Col span={2}><h1>添加好友:</h1></Col>
                    <Col span={5}>
                    <Search placeholder="请输入邮箱名或用户名关键字" onSearch={value => this.handleSearch(value)} enterButton />
                    </Col>
                    <Col span={2}/>
                    <Col span={2}>
                    <Button type="primary" onClick={() => this.handleCheckRequest()}>查看待处理申请</Button>
                    </Col>
                    <Modal
                     title='搜索列表'
                     visible={searchVisible}
                     onCancel={this.handleBack}
                     destroyOnClose={true}
                     footer={[
                        <Button key="back" onClick={this.handleBack}>
                          返回
                        </Button>
                      ]}
                    >
                        <List
                          itemLayout="horizontal"
                          dataSource={searchList}
                          locale={{emptyText: "没有符合条件用户"}}
                          width={443}
                          height={328}
                          style={{ overflowY: "scroll", height: "100%"}}
                          renderItem={item => (
                         <List.Item
                         actions={[<Button type="primary" onClick={() => this.addRequest(item.account)}>添加</Button>]}
                         >
                         <List.Item.Meta
                           avatar={<Avatar src={`/chat/images/avatar/${item.icon}`}/>}
                           title={item.name}
                           description={item.mail}
                        />
                       </List.Item>
                        )}
                     />
                    </Modal>
                    <Modal
                     title='待处理申请列表'
                     visible={requestVisible}
                     onCancel={this.handleRequestBack}
                     destroyOnClose={true}
                     footer={[
                        <Button key="back" onClick={this.handleRequestBack}>
                          返回
                        </Button>
                      ]}
                    >
                        <List
                          itemLayout="horizontal"
                          dataSource={requestList}
                          locale={{emptyText: "没有待处理申请"}}
                          width={443}
                          height={328}
                          style={{ overflowY: "scroll", height: "100%"}}
                          renderItem={item => (
                         <List.Item
                         actions={[<Button type="primary" onClick={() => this.operateRequest(item.id, REQUEST_AGREE)}>同意</Button>, <Button type="danger" onClick={() => this.operateRequest(item.id, REQUEST_DISAGREE)}>忽略</Button>]}
                         >
                         <List.Item.Meta
                           avatar={<Avatar src={`/chat/images/avatar/${item.userAvatar}`}/>}
                           title={item.userName}
                           description={moment(item.time * 1000).format('YYYY-MM-DD HH:mm:ss')}
                        />
                       </List.Item>
                        )}
                     />
                    </Modal>
                </Row>
                <Divider/>
                <h1>好友列表:</h1>
                <Table columns={columns} dataSource={friends}/>
            </div>
        )
    }
}
export default Contacts