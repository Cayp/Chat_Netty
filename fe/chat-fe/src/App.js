import React from 'react';
import { Route, Link, Switch, withRouter, Redirect, } from "react-router-dom";
import { Layout, Menu, Breadcrumb } from 'antd';
import { LoginUser } from '../src/auth/LoginUser'
import './App.css'
import 'antd/dist/antd.css'
import {
  WechatOutlined,
  GlobalOutlined,
  UserOutlined,
  SolutionOutlined,
} from '@ant-design/icons';
const { Header, Content, Footer, Sider } = Layout;
class App extends React.Component {

  constructor(props) {
    super(props);
    this.state = {
      collapsed: false,
      authenticated: false,
    };
  }

  onCollapse = collapsed => {
    console.log(collapsed);
    this.setState({ collapsed });
  };

  render() {
    return (
      <Layout style={{ minHeight: '100vh' }}>
        <Sider collapsible collapsed={this.state.collapsed} onCollapse={this.onCollapse}>
          <div className="logo" />
          <Menu theme="dark" defaultSelectedKeys={['1']} mode="inline">
            <Menu.Item key="1">
            <WechatOutlined />
              <span>聊天</span>
            </Menu.Item>
            <Menu.Item key="2">  
            <GlobalOutlined />
              <span>朋友动态</span>
            </Menu.Item>
            <Menu.Item key="3">
            <SolutionOutlined />
             <span>通讯录</span>
            </Menu.Item>
            <Menu.Item key="4">
            <UserOutlined />
             <span>个人信息</span>
            </Menu.Item>
          </Menu>
        </Sider>
        <Layout className="site-layout">
        <Header style={{ background: '#fff', padding: 0 }}>
            <LoginUser />
          </Header>
          <Content style={{ margin: '0 16px' }}>
            <br/>
            <div className="site-layout-background" style={{ padding: 24, minHeight: 360 }}>
              test.
            </div>
          </Content>
          <Footer style={{ textAlign: 'center' }}>©2020 Created by linjianpeng</Footer>
        </Layout>
      </Layout>
    )
  }
}


export default App;
