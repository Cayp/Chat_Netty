import React from 'react'
import { Modal, Input, Form, message } from 'antd'
import { myAxios } from '../utils/myAxios'
import { getUser } from '../utils/util'
const form = Form.create({})

@form
class EditPasswordModal extends React.Component {
    handleCancel = () => {
        this.props.form.resetFields()
        this.props.toggleVisible(false)
    }
    /**
     * 模态框的确定按钮
     */
    handleOk = () => {
        this.props.form.validateFields((err, values) => {
            if (!err) {
                this.onSubmit(values)
            }
        });
    }
    /**
     * 提交修改密码
     */
    onSubmit = async (values) => {  
            console.log(values)
            const res = await myAxios.post('/chat/user/changePassWord', {
                password: values.password
            })
            if (res.data.code === 20000) {
                message.success('修改密码成功')
                this.handleCancel()
            }
        
    }

    render() {
        const { visible } = this.props
        const { getFieldDecorator, getFieldValue } = this.props.form
        const user = getUser()
        const formItemLayout = {
            labelCol: { span: 6 },
            wrapperCol: { span: 14 },
        }
        return (
            <Modal
                onCancel={this.handleCancel}
                onOk={this.handleOk}
                visible={visible}
                centered
                title="修改密码">
                <Form>
                    <Form.Item label={'用户名'} {...formItemLayout}>
                        {getFieldDecorator('username', {initialValue: user.name})(
                            <Input disabled />
                        )}
                    </Form.Item>
                    <Form.Item label={'新密码'} {...formItemLayout}>
                        {getFieldDecorator('password', {
                            validateFirst: true,
                            rules: [
                                { required: true, message: '密码不能为空' },
                                { pattern: '^[^ ]+$', message: '密码不能有空格' },
                                { min: 3, message: '密码至少为3位' },
                            ]
                        })(
                            <Input
                                placeholder="请输入新密码"
                                autoComplete="new-password"
                                type={'password'} />
                        )}
                    </Form.Item>
                    <Form.Item label={'确认密码'} {...formItemLayout}>
                        {getFieldDecorator('confirmPassword', {
                            validateFirst: true,
                            rules: [
                                { required: true, message: '请确认密码' },
                                {
                                    validator: (rule, value, callback) => {
                                        if (value !== getFieldValue('password')) {
                                            callback('两次输入不一致！')
                                        }
                                        callback()
                                    }
                                },
                            ]
                        })(
                            <Input
                                onPressEnter={this.handleOk}
                                placeholder="请确认密码"
                                autoComplete="new-password"
                                type={'password'} />
                        )}
                    </Form.Item>
                </Form>
            </Modal>
        )
    }
}

export default EditPasswordModal