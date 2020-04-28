import React from 'react'
import { Icon, message, Menu, Avatar } from 'antd'
import { withRouter } from 'react-router-dom'
import { connect } from 'react-redux'
import LoadableComponent from '../utils/LoadableComponent'
import { myAxios } from '../utils/myAxios'
const SubMenu = Menu.SubMenu;
const MenuItemGroup = Menu.ItemGroup;

// const EditInfoModal = LoadableComponent(import('./EditInfoModal'))
// const EditPasswordModal = LoadableComponent(import('./EditPasswordModal'))

const store = connect(
    (state) => ({ user: state.user })
)

@withRouter @store
class MyHeader extends React.Component {
    constructor(props) {
        super(props);
        const userTheme = JSON.parse(localStorage.getItem('user-theme'))
        let color = '#13C2C2'
        if (userTheme) {
            window.less.modifyVars(userTheme)
            color = userTheme['@primary-color']
        }
        this.state = {
            color: color,
            infoVisible: false,     //控制修改用户信息的模态框
            passwordVisible: false   //控制修改密码的模态框
        }
    }
    /**
     * 切换侧边栏的折叠和展开
     */
    toggleCollapsed = () => {
        this.props.onChangeState({
            collapsed: !this.props.collapsed
        })
    }

    /**
     * 切换主题
     */
    changeColor = (color) => {
        window.less.modifyVars({
            '@primary-color': color,
        }).then(() => {
            localStorage.setItem('user-theme', JSON.stringify({ '@primary-color': color }))
            this.setState({
                color
            })
            message.success('更换主题颜色成功')
        })
    }
    /**
     * 重置主题
     */
    resetColor = () => {
        this.changeColor('#13C2C2')
    }
    /**
     * 展开/关闭修改信息模态框
     */
    toggleInfoVisible = (visible) => {
        this.setState({
            infoVisible: visible
        })
    }
    /**
     * 展开/关闭修改密码模态框
     */
    togglePasswordVisible = (visible) => {
        this.setState({
            passwordVisible: visible
        })
    }
    /**
     * 退出登录
     */
    onLogout = () => {
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

    changeTheme = () => {
        const theme = this.props.theme === 'dark' ? 'light' : 'dark'
        localStorage.setItem('theme', theme)
        this.props.onChangeState({
            theme
        })
    }

    render() {
        const {infoVisible, passwordVisible } = this.state
        const {  theme } = this.props
        const userName = sessionStorage.getItem("userName")
        return (
            <div style={{ background: '#fff', padding: '0 16px' }}>
                <Icon
                    style={{ fontSize: 18 }}
                    type={this.props.collapsed ? 'menu-unfold' : 'menu-fold'}
                    onClick={this.toggleCollapsed}
                />
                <div style={styles.headerRight}>
                    <div style={styles.headerItem}>
                        <Icon type={theme === 'dark' ? 'icon-yueliang1' : 'icon-taiyang'} style={{ fontSize: 24 }} onClick={this.changeTheme}/>
                        {/* <img width={24} src={require(`../../assets/images/${theme}.svg`)} alt='' onClick={this.changeTheme} /> */}
                    </div>
                    <div style={styles.headerItem}>
                        <Menu mode="horizontal" selectable={false}>
                            <SubMenu title={<div style={styles.avatarBox}><Avatar size='small' src={require("../images/user.jpg")} />&nbsp;<span>{userName}</span></div>}>
                                <MenuItemGroup title="用户中心">
                                   <Menu.Item key={1} onClick={() => {}}><Icon type="user" />编辑个人信息</Menu.Item>
                                   <Menu.Item key={77} onClick={() => {}}><Icon type="edit" />修改密码</Menu.Item>
                                    <Menu.Item key={2} onClick={this.onLogout}><Icon type="logout" />退出登录</Menu.Item>
                                </MenuItemGroup>
                            </SubMenu>
                        </Menu>
                    </div>
                </div>
                {/* <EditInfoModal toggleVisible={this.toggleInfoVisible} visible={infoVisible} />
                <EditPasswordModal toggleVisible={this.togglePasswordVisible} visible={passwordVisible} /> */}
            </div>
        )
    }
}

const styles = {
    headerRight: {
        float: 'right',
        display: 'flex',
        height: 64,
        marginRight: 50
    },
    headerItem: {
        display: 'flex',
        alignItems: 'center',
        padding: '0 20px'
    },
    avatarBox: {
        display: 'flex',
        alignItems: 'center',
    }
}

export default MyHeader