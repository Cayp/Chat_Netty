import React from 'react';
import { Form, Input, Button, Checkbox, message} from 'antd';
import { Route, Link, Switch, withRouter, Redirect, } from "react-router-dom"; 
import { myAxios } from '../utils/myAxios'
import '../App.css'

const layout = {
    labelCol: { span: 8 },
    wrapperCol: { span: 16 },
  };
  const tailLayout = {
    wrapperCol: { offset: 8, span: 16 },
  };
 
class Login extends React.Component {
    constructor(props) {
        super(props);
        const values = JSON.parse(localStorage.getItem("values"))
        this.state = {
            account:  values ? values.account : undefined,
            password: values ? values.password : undefined,
            remember: values ? values.remember : true,
        }
    }

    onFinish = values => {
      console.log(values)
      if (values.remember) {
        localStorage.setItem("values", JSON.stringify(values))
      } else {
        localStorage.removeItem("values")
      }
      myAxios.post('/chat/user/login', values)
             .then((respone) => {
               let json = respone.data
               if (json.code == 20000) {
                message.success(json.message)
                window.sessionStorage.setItem("userId", json.data.account);
                window.sessionStorage.setItem("userName", json.data.name);
                this.props.history.push("/home")
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
              initialValues={{account: this.state.account, password: this.state.password, remember: this.state.remember}}
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
        
              <Form.Item {...tailLayout} name="remember" valuePropName="checked" >
                <Checkbox>记住账号密码</Checkbox>
              </Form.Item>
        
              <Form.Item {...tailLayout}>
                <Button type="primary" htmlType="submit">
                  登录
                </Button>
                &nbsp;&nbsp;&nbsp;&nbsp;
                <Link to="/auth/register">去注册</Link>
                &nbsp;&nbsp;&nbsp;&nbsp;
                <Link to="/auth/findPassword">忘记密码</Link>
              </Form.Item>
            </Form> 
     );
    }
}
export default withRouter(Login);
