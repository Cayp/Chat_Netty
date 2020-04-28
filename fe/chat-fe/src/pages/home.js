import React from 'react';
import { Route, Link, Switch, withRouter, Redirect} from "react-router-dom";
import { Layout, Menu, Breadcrumb, Icon} from 'antd';
import '../App.css'
import 'antd/dist/antd.css'
import {
  WechatOutlined,
  GlobalOutlined,
  UserOutlined,
  SolutionOutlined,
} from '@ant-design/icons';
import LoginUser from '../auth/LoginUser';
import { initWebSocket } from '../store/actions'
import { connect } from 'react-redux'
import { bindActionCreators } from 'redux'
import MySider from './MySider';
import MyContent from './MyContent';
import MyHeader from './MyHeader';
const { Header, Content, Footer, Sider } = Layout;


const store = connect(
    (state) => ({  websocket: state.websocket }),
    (dispatch) => bindActionCreators({initWebSocket }, dispatch)
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
        const userId = sessionStorage.getItem('userId')
        this.props.initWebSocket(userId)
    }

  onCollapse = collapsed => {
    console.log(collapsed);
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
          <Footer style={{ textAlign: 'center' }}>©2020 Created by linjianpeng</Footer>
        </Layout>
      </Layout>
    )
  }
}


export default withRouter(Home);
