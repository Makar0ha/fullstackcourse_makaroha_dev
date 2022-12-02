import {Avatar} from "antd";
import {UserOutlined} from "@ant-design/icons";
import React from "react";

const TheAvatar = ({name}) => {
    let tr = name.trim();
    if (tr.length === 0) {
        return <Avatar icon={<UserOutlined/>}/>
    }
    const split = tr.split(" ");
    if (split.length === 1) {
        return <Avatar>{name.charAt(0)}</Avatar>
    }
    return <Avatar>{`${name.charAt(0)}${name.charAt(name.length - 1)}`}</Avatar>
}

export default TheAvatar;