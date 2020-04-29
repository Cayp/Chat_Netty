import React from 'react';
import { Form, Input, Button, Checkbox, message, Icon} from 'antd';
import { Route, Link, Switch, withRouter, Redirect, } from "react-router-dom"; 
import { myAxios } from '../utils/myAxios'
import '../App.css'


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

    handleSubmit = e => {
      e.preventDefault();
    this.props.form.validateFields((err, values) => {
      if (!err) {
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
                let user = {id: json.data.account, name: json.data.name, avator: json.data.avator}
                sessionStorage.setItem("user", JSON.stringify(user))
                this.props.history.push("/home")
               } else {
                message.error(json.message)
               }
           })
      }
    });
      
  };
    
    render() {
        const { getFieldDecorator } = this.props.form;
        let { account, password, remember } = this.state;
        return ( 
          <Form onSubmit={this.handleSubmit} className="login-form">
          <Form.Item>
            {getFieldDecorator('account', {
              initialValue: account,
              rules: [{ required: true, message: '请输入邮箱名!' }],
            })(
              <Input
                prefix={<Icon type="user" style={{ color: 'rgba(0,0,0,.25)' }} />}
                placeholder="请输入邮箱名"
              />,
            )}
          </Form.Item>
          <Form.Item>
            {getFieldDecorator('password', {
              initialValue: password,
              rules: [{ required: true, message: '请输入密码!' }],
            })(
              <Input
                prefix={<Icon type="lock" style={{ color: 'rgba(0,0,0,.25)' }} />}
                type="password"
                placeholder="请输入密码"
              />,
            )}
          </Form.Item>
          <Form.Item>
            {getFieldDecorator('remember', {
              valuePropName: 'checked',
              initialValue: remember,
            })(<Checkbox>记住账号密码</Checkbox>)}
            <Link to="/auth/findPassword" className="login-form-forgot">
            &nbsp;&nbsp;忘记密码&nbsp;&nbsp;&nbsp;&nbsp;</Link>
            <Button type="primary" htmlType="submit" className="login-form-button">
              登录
            </Button>
            &nbsp;&nbsp;Or <Link to="/auth/register">去注册</Link>
          </Form.Item>
        </Form>
     );
    }
}

const WrappedNormalLoginForm = Form.create({ name: 'normal_login' })(withRouter(Login));
export default WrappedNormalLoginForm
