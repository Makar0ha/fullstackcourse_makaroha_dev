import '../css/App.css';
import React, { useEffect, useState } from "react";
import { Breadcrumb, Layout, Menu, Spin, Empty, Button, Image } from 'antd';
import {
    DesktopOutlined,
    FileOutlined,
    PieChartOutlined,
    PlusOutlined,
    TeamOutlined,
    UserOutlined,
} from '@ant-design/icons';
import { getAllStudents } from "./client";
import StudentDrawerForm from "./StudentDrawerForm";
import { errorNotification } from "./nottification";
import StudentsTable from './StudentsTable';

const { Header, Content, Footer, Sider } = Layout;
const items = [
    getItem('Option 1', '1', <PieChartOutlined />),
    getItem('Option 2', '2', <DesktopOutlined />),
    getItem('User', 'sub1', <UserOutlined />, [
        getItem('Tom', '3'),
        getItem('Bill', '4'),
        getItem('Alex', '5'),
    ]),
    getItem('Team', 'sub2', <TeamOutlined />, [getItem('Team 1', '6'), getItem('Team 2', '8')]),
    getItem('Files', '9', <FileOutlined />),
];

function getItem(label, key, icon, children) {
    return {
        key,
        icon,
        children,
        label,
    };
}

function MainBoard() {
    const [collapsed, setCollapsed] = useState(false);
    const [students, setStudents] = useState([]);
    const [fetching, setFetching] = useState(true);
    const [showDrawer, setShowDrawer] = useState(false);

    const fetchStudents = () =>
        getAllStudents()
            .then(res => res.json())
            .then(data => {
                setStudents(data);
            })
            .catch(err => {
                err.response.json().then(res => {
                    errorNotification("There was an issue", `${res.message} [${res.status}] [${res.error}]`);
                });
            })
            .finally(() => {
                setFetching(false);
            });

    useEffect(() => {

        fetchStudents();
    }, [])

    const renderStudents = () => {
        if (fetching) {
            return <Spin size="large" />
        }
        if (students.length <= 0) {
            return <>
                <StudentDrawerForm
                    showDrawer={showDrawer}
                    setShowDrawer={setShowDrawer}
                    fetchStudents={fetchStudents}
                />
                <Empty />
            </>
        }
        return <>
            <StudentDrawerForm
                showDrawer={showDrawer}
                setShowDrawer={setShowDrawer}
                fetchStudents={fetchStudents}
            />
            <StudentsTable
                students={students}
                fetchStudents={fetchStudents}
            />
        </>
    }

    return <Layout
        style={{
            minHeight: '100vh',
        }}
    >
        <Sider collapsible collapsed={collapsed} onCollapse={(value) => setCollapsed(value)}>
            <div className="logo" />
            <Menu theme="dark" defaultSelectedKeys={['1']} mode="inline" items={items} />
        </Sider>
        <Layout className="site-layout">
            <Header
                className="site-layout-background"
                style={{
                    padding: 0,
                }}
            />
            <Content
                style={{
                    margin: '0 16px',
                }}
            >
                <Breadcrumb
                    style={{
                        margin: '16px 0',
                    }}
                >
                    <Breadcrumb.Item>User</Breadcrumb.Item>
                    <Breadcrumb.Item>Bill</Breadcrumb.Item>
                </Breadcrumb>
                <>
                    <Button
                        onClick={() => setShowDrawer(!showDrawer)}
                        type="primary" shape="round" icon={<PlusOutlined />} size="small">
                        Add new Student
                    </Button>

                </>

                <div
                    className="site-layout-background"
                    style={{
                        padding: 24,
                        minHeight: 360,
                    }}
                >
                    {renderStudents()}
                </div>
            </Content>
            <Footer
                style={{
                    textAlign: 'center',
                }}
            >
                <Image
                    width={75}
                    src="https://user-images.githubusercontent.com/77810752/205652652-d1239b3b-75b3-4b8f-8656-b20765b3e9f9.png"
                />
                <div>Makar0ha </div>
            </Footer>
        </Layout>
    </Layout>
}

export default MainBoard;
