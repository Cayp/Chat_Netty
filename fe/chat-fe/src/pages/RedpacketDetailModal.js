import React from "react";
import { Modal, Divider, Avatar, List, Button } from "antd";
import { myAxios } from "../utils/myAxios";
import moment from 'moment'

class RedpacketDetailModal extends React.Component {
    constructor(props) {
        super(props);
    }

    handleBack = () => {
         this.props.handleDetailBack(false);
    }

    render() {
        const { visible, detail, userId, grapsList } = this.props;
        const userGrap = grapsList.find(item => item.userId == userId);
        const message = userGrap ? `你已抢到${userGrap.money}元` : '你没有抢到'
        return (
            <Modal
             title='红包详情'
             visible={visible}
             onCancel={this.handleBack}
             destroyOnClose={true}
             footer={[
                <Button key="back" onClick={this.handleBack}>
                  返回
                </Button>
              ]}
             >
             <h>{message}</h>
             &nbsp;&nbsp;&nbsp;
             <span>总金额:{detail.total_money},已抢:{grapsList.length}/{detail.redPacket_size}</span>
             <Divider/>
             <div style={{height: "300px"}}>
             <List
             itemLayout="horizontal"
             dataSource={grapsList}
             locale={{emptyText: "暂无人抢"}}
             width={443}
             height={328}
             style={{ overflowY: "scroll", height: "100%"}}
             renderItem={item => (
                <List.Item>
                <List.Item.Meta
                avatar={<Avatar src={`/chat/images/avatar/${item.userAvatar}`}/>}
                title={item.userName}
                description={moment(item.time * 1000).format('YYYY-MM-DD HH:mm:ss')}
                />
                <div>{item.money}元</div>
            </List.Item>
              ) }
             />
             
             </div>
            </Modal>
        )
    }
}
export default RedpacketDetailModal