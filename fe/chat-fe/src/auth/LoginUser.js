import React from 'react';
import { Menu, Row, Col, message,Button} from 'antd';
import { Route, Link, Switch, withRouter, Redirect,  BrowserRouter as Router } from "react-router-dom";

import {
  UserOutlined
} from '@ant-design/icons';
import { myAxios } from '../utils/myAxios';

const SubMenu = Menu.SubMenu;


class LoginUser extends React.Component {
    constructor(props) {
      super(props);
    }

logout = () => {
   myAxios.get('/chat/user/logout')
          .then((response) => {
            let json = response.data;
            if (json.code == 20000) {
              message.success(json.message)
              this.props.history.push('/auth/login')
            } else {
              message.error(json.message)
            }
        })
}

render()　{
      const userName = window.sessionStorage.getItem("userName")
      const userId = window.sessionStorage.getItem("userId")
      return (
        <Row type="flex" justify="end">
          <Col>
            <Menu mode="horizontal" style={{ lineHeight: '64px' }}>
              {userName && userId
                ? (
                  <SubMenu title={<span className="submenu-title-wrapper"><UserOutlined/>Hi, {userName}</span>}>
                    <Menu.Item >
                      <Button type="link" htmlType="button" onClick={this.logout}>退出登录</Button>
                    </Menu.Item>
                  </SubMenu>
                )
                : <Link to="/auth/login">登录</Link>
              }
            </Menu>
          </Col>   
        </Row>
        )
  }
}
export default withRouter(LoginUser);