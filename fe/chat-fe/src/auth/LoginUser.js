import React from 'react';
import { Menu, Row, Col } from 'antd';
import { AuthConsumer } from "../auth/AuthContext";
import { Route, Link, Switch, withRouter, Redirect,  BrowserRouter as Router } from "react-router-dom";
import { Login } from './Login'
import {
  UserOutlined
} from '@ant-design/icons';

const SubMenu = Menu.SubMenu;

const LoginUser = props => (
  <AuthConsumer>
    {({ user, authenticated}) => {
      return (
        <Row type="flex" justify="end">
          <Col>
            <Menu mode="horizontal" style={{ lineHeight: '64px' }}>
              {authenticated && user.name
                ? (
                  <SubMenu title={<span className="submenu-title-wrapper"><UserOutlined/>Hi, {user.name}</span>}>
                    <Menu.Item >
                      <a href="/logout">Logout</a>
                    </Menu.Item>
                  </SubMenu>
                )
                : <Link to="/login">login</Link>
              }
            </Menu>
          </Col>
          <Route path="/login" component={Login}></Route>
        </Row>
        )
    }}
  </AuthConsumer>
)

export { LoginUser };