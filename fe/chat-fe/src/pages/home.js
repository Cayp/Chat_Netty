import React from 'react';
import { withRouter} from "react-router-dom";
import { Layout} from 'antd';
import '../App.css'
import 'antd/dist/antd.css'
import { initWebSocket, initChatListsMap, initLeftItemList,setUser} from '../store/actions'
import { connect } from 'react-redux'
import { bindActionCreators } from 'redux'
import MySider from './MySider';
import MyContent from './MyContent';
import MyHeader from './MyHeader';
import { getUser } from '../utils/util';
const { Header, Sider } = Layout;


const store = connect(
    (state) => ({  user: state.user, websocket: state.websocket, leftItemList: state.leftItemList, chatListsMap: state.chatListsMap}),
    (dispatch) => bindActionCreators({initWebSocket, initChatListsMap, initLeftItemList, setUser}, dispatch)
)

@store
class Home extends React.Component {

  constructor(props) {
    super(props);
    this.state = {
      collapsed: false,
      authenticated: false,
      panes: [],    //网站打开的标签页列表
      activeMenu: '',  //网站活动的菜单
      theme: localStorage.getItem('theme') || 'dark',   //侧边栏主题
    };
}
componentDidMount() {
    this.init()
}

componentWillUnmount() {
    const websocket = this.props.websocket
    websocket && websocket.close()
}

/**
     * 初始化用户信息和建立websocket连接
     */
    init = async () => {
        const user = this.props.user
        const user1 = getUser()
        const userInfo = user == null ? user1 : user
        if (userInfo) {
          if (user == null) {
           this.props.setUser(userInfo)
          }
          this.props.initWebSocket(userInfo.id)
          this.props.initLeftItemList(userInfo.id)
          this.props.initChatListsMap(userInfo.id)
          return
        }
        this.props.history.push("/auth/login")
    }

  onCollapse = collapsed => {
    this.setState({ collapsed });
  };

  _setState = (obj) => {
    this.setState(obj)
}

  render() {
    const { collapsed, panes, activeMenu, theme } = this.state
    return (
      <Layout style={{ minHeight: '100vh' }}>
        <Sider trigger={null} collapsible collapsed={collapsed} theme={theme}>
                   <MySider
                    theme={theme}
                    panes={panes}
                    activeMenu={activeMenu}
                    onChangeState={this._setState}
                   />
                </Sider>
        <Layout className="site-layout">
        <Header style={{ padding: 0 }}>
                       <MyHeader
                             theme={theme}
                             collapsed={collapsed}
                             onChangeState={this._setState}
                       />
                    </Header>
         <MyContent 
              panes={panes}
              activeMenu={activeMenu}
              onChangeState={this._setState}/>
        </Layout>
      </Layout>
    )
  }
}


export default withRouter(Home);
