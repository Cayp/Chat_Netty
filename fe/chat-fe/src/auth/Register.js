import React from 'react';
import {
    Form,
    Input,
    Tooltip,
    Icon,
    Cascader,
    Select,
    Row,
    Col,
    Checkbox,
    Button,
    AutoComplete,
    message
  } from 'antd';
import { myAxios } from '../utils/myAxios';
import '../App.css';
import { Link } from "react-router-dom"; 
const form = Form.create({})

@form
class Register extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            confirmDirty: false,
            autoCompleteResult: [],
        }
    }
    
    sendMail = () => {
        const mail = this.props.form.getFieldValue('mail')
        console.log(mail)
        if (mail) {
            const params = {mail: mail, type: 1001}  
            myAxios.post('/chat/verify/sendMail', params)
                 .then((response) => {
                     let json = response.data;
                     if (json.code == 20000) {
                         message.success("发送邮箱验证码成功")
                     } else {
                         message.error(json.message)
                     }
                 })
        } else {
            message.error("请输入正确的邮箱格式")
        }
    }
    handleSubmit = e => {
        e.preventDefault();
        this.props.form.validateFieldsAndScroll((err, values) => {
          if (!err) {
            console.log('Received values of form: ', values);
            myAxios.post('/chat/verify/register', values)
                   .then((response) => {
                    let json = response.data
                    if (json.code == 20000) {
                        message.success("注册成功")
                        this.props.form.resetFields();
                    } else {
                        message.error(json.message)
                    }
                   })
          }
        });
      };
    
      handleConfirmBlur = e => {
        const { value } = e.target;
        this.setState({ confirmDirty: this.state.confirmDirty || !!value });
      };
    
      compareToFirstPassword = (rule, value, callback) => {
        const { form } = this.props;
        if (value && value !== form.getFieldValue('password')) {
          callback('两次密码不一致!');
        } else {
          callback();
        }
      };
    
      validateToNextPassword = (rule, value, callback) => {
        const { form } = this.props;
        if (value && this.state.confirmDirty) {
          form.validateFields(['confirm'], { force: true });
        }
        callback();
      };

    render() {
        const { getFieldDecorator } = this.props.form;
        const { autoCompleteResult } = this.state;    
        const formItemLayout = {
            labelCol: {
              xs: { span: 24 },
              sm: { span: 8 },
            },
            wrapperCol: {
              xs: { span: 24 },
              sm: { span: 16 },
            },
          };
          const tailFormItemLayout = {
            wrapperCol: {
              xs: {
                span: 24,
                offset: 0,
              },
              sm: {
                span: 16,
                offset: 8,
              },
            },
          };
          return (
            <Form {...formItemLayout} onSubmit={this.handleSubmit} className="">
              <Form.Item label="邮箱">
                {getFieldDecorator('mail', {
                  rules: [
                    {
                      type: 'email',
                      message: '不是邮箱格式!',
                    },
                    {
                      required: true,
                      message: '请输入你的邮箱!',
                    },
                  ],
                })(<Input />)}
              </Form.Item>
              <Form.Item label="密码" hasFeedback>
                {getFieldDecorator('password', {
                  rules: [
                    {
                      required: true,
                      message: '请输入你的密码!',
                    },
                    {
                      validator: this.validateToNextPassword,
                    },
                  ],
                })(<Input.Password />)}
              </Form.Item>
              <Form.Item label="再次确认密码" hasFeedback>
                {getFieldDecorator('confirm', {
                  rules: [
                    {
                      required: true,
                      message: '请再次输入你的密码!',
                    },
                    {
                      validator: this.compareToFirstPassword,
                    },
                  ],
                })(<Input.Password onBlur={this.handleConfirmBlur} />)}
              </Form.Item>
              <Form.Item
                label={
                  <span>
                    用户名&nbsp;
                    <Tooltip title="你的用户名">
                      <Icon type="question-circle-o" />
                    </Tooltip>
                  </span>
                }
              >
                {getFieldDecorator('name', {
                  rules: [{ required: true, message: '请输入你的用户名!', whitespace: true }],
                })(<Input />)}
              </Form.Item>
              <Form.Item label="邮箱验证码" extra="我们需要确认你是该邮箱使用者.">
                <Row gutter={8}>
                  <Col span={12}>
                    {getFieldDecorator('verifycode', {
                      rules: [{ required: true, message: '请输入邮箱验证码!' }],
                    })(<Input />)}
                  </Col>
                  <Col span={12}>
                    <Button onClick={this.sendMail}>获取邮箱验证码</Button>
                  </Col>
                </Row>
              </Form.Item>
              <Form.Item {...tailFormItemLayout}>
                <Button type="primary" htmlType="submit">
                  注册
                </Button>
                &nbsp;&nbsp;&nbsp;
                <Link to="/auth/login">去登录</Link>
              </Form.Item>
            </Form>
          );
        }
}
export default Register