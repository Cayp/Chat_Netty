import React from 'react'
import { Modal, Form, Upload, Icon, message, Input, Radio, DatePicker, Alert } from 'antd'
import { getUser, storeUser } from '../utils/util'
import Cookies from 'js-cookie'
import { setUser, initWebSocket } from '../store/actions'
import { connect, } from 'react-redux'
import { bindActionCreators } from 'redux'

const store = connect(
    (state) => ({ user: state.user }),
    (dispatch) => bindActionCreators({ setUser }, dispatch)
)
const form = Form.create({})

@store@form
class EditInfoModal extends React.Component {
    constructor(props) {
        super(props);
        const user = this.props.user
        this.state = {
            uploading: false,
            avatar: user.avatar,
            username: user.name
        }
    }
    /**
     * 关闭模态框
     */
    handleCancel = () => {
        this.props.toggleVisible(false)
    }
   

    render() {
        const { uploading, avatar} = this.state
        const { getFieldDecorator} = this.props.form
        const formItemLayout = {
            labelCol: { span: 6 },
            wrapperCol: { span: 14 },
        }
        const { visible } = this.props
        const uploadProps = {
            name: "picture",
            listType: "picture-card",
            headers: {
                authorization: Cookies.get("authorization"),
            },
            action: `/chat/user/avatar`,
            method: 'PUT',
            accept: ".jpg",
            showUploadList: false,
            onChange: (info) => {
                if (info.file.status !== 'uploading') {
                    this.setState({
                        uploading: true
                    })
                }
                if (info.file.status === 'done') {
                    this.setState({
                        uploading: false,
                        avatar: info.file.response.data.url
                    })
                    const user = this.props.user
                    const newUser = { avatar: info.file.response.data.url, id: user.id, name: user.name }
                    this.props.setUser(newUser)
                    storeUser(newUser)
                    message.success('上传头像成功')
                } else if (info.file.status === 'error') {
                    this.setState({
                        uploading: false
                    })
                    message.error(info.file.response.message || '上传头像失败')
                }
            }
        }
        return (
                <div style={{ height: '60vh', overflow: 'auto' }}>
                    <Modal
                     onCancel={this.handleCancel}
                     onOk={this.handleCancel}
                     visible={visible}
                     centered
                     title="修改个人信息">
                    <Form>
                        <Form.Item label={'头像'} {...formItemLayout}>
                            {getFieldDecorator('avatar', {
                                rules: [{ required: true, message: '请上传用户头像' }],
                            })(
                                <Upload {...uploadProps} style={styles.avatarUploader}>
                                    {this.props.user.avatar? <img src={`/chat/images/avatar/${avatar}`} alt="avatar" style={styles.avatar} /> : <Icon style={styles.icon} type={uploading ? 'loading' : 'plus'} />}
                                </Upload>
                            )}
                        </Form.Item>
                        <Form.Item label={'用户名'} {...formItemLayout}>
                            {getFieldDecorator('username', {
                                validateFirst: true,
                                initialValue:this.state.username,
                                rules: [
                                    { required: false }
                                ]
                            })(
                                <Input disabled />
                            )}
                        </Form.Item>
                    </Form>
                    </Modal>
                </div>
        )
    }
}

const styles = {
    avatarUploader: {
        display: 'flex',
        justifyContent: 'center',
        alignItems: 'center',
        width: 150,
        height: 150,
        backgroundColor: '#fff'
    },
    icon: {
        fontSize: 28,
        color: '#999'
    },
    avatar: {
        maxWidth: '100%',
        maxHeight: '100%',
    },
}

export default EditInfoModal