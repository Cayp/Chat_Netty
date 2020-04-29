import React from 'react';
import { Menu, Row, Col, message,Button} from 'antd';
import { Route, Link, Switch, withRouter, Redirect,  BrowserRouter as Router } from "react-router-dom";

import {
  UserOutlined
} from '@ant-design/icons';
import { myAxios } from '../utils/myAxios';
import { getUser } from '../utils/util'
const SubMenu = Menu.SubMenu;


class LoginUser extends React.Component {
    constructor(props) {
      super(props);
    }

logout = () => {
 
}

render()　{
      const user = getUser()
      return (
        <Row type="flex" justify="end">
          <Col>
            <Menu mode="horizontal" style={{ lineHeight: '64px' }}>
              {user ? 
               (
                  <SubMenu title={<span className="submenu-title-wrapper"><UserOutlined/>Hi, {user.name}</span>}>
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