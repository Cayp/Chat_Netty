import React from 'react';
import { Form, Input, Button, Checkbox, message} from 'antd';
import { Route, Link, Switch, withRouter, Redirect, } from "react-router-dom"; 
import { myAxios } from '../utils/myAxios'
import '../App.css'
import {
    UserOutlined,
    LockOutlined,
  } from '@ant-design/icons';
const layout = {
    labelCol: { span: 8 },
    wrapperCol: { span: 16 },
  };
  const tailLayout = {
    wrapperCol: { offset: 8, span: 16 },
  };
 

export class Login extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            
        }
    }

    onFinish = values => {
      myAxios.post('/chat/user/login', values)
             .then((respone) => {
               let json = respone.data
               if (json.code == 20000) {
                message.success(json.message)
                window.sessionStorage.setItem("userId", json.data.account);
                window.sessionStorage.setItem("userName", json.data.name);
               } else {
                message.error(json.message)
               }
           })
  };

    onFinishFailed = errorInfo => {
    console.log('Failed:', errorInfo);
  };
    
    render() {
        return (  
             <Form
              {...layout}
              name="basic"
              initialValues={{ remember: true }}
              onFinish={this.onFinish}
              onFinishFailed={this.onFinishFailed}
            >
              
              <Form.Item
                label="账号"
                name="account"
                rules={[{ required: true, message: '请输入邮箱名!' }]}
              >
                <Input placeholder="请输入邮箱"/>
              </Form.Item>
        
              <Form.Item
                label="密码"
                name="password"
                rules={[{ required: true, message: '请输入密码!' }]}
              >
                <Input.Password placeholder="请输入密码"/>
              </Form.Item>
        
              <Form.Item {...tailLayout} name="remember" valuePropName="checked">
                <Checkbox>记住密码</Checkbox>
                <Link to="/auth/findPassword">忘记密码？</Link>
              </Form.Item>
        
              <Form.Item {...tailLayout}>
                <Button type="primary" htmlType="submit">
                  登录
                </Button>
                &nbsp;&nbsp;&nbsp;&nbsp;
                <Link to="/auth/register">去注册</Link>
              </Form.Item>
            </Form> 
     );
    }
}
