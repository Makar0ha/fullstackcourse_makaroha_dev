import { Badge, Form, Input, Popconfirm, Radio, Select, Table, Tag, Typography } from "antd";
import React, { useEffect, useState } from "react";
import { deleteStudent, editStudent } from "./client";
import { errorNotification, successNotification } from "./nottification";
import TheAvatar from "./TheAvatar";


const removeStudent = (studentId, fetchStudents) => {
    deleteStudent(studentId).then(() => {
        successNotification("Student deleted", `Student with id - ${studentId} was deleted`);
        fetchStudents();
    }).catch(err => {
        err.response.json().then(res => {
            errorNotification("There was an issue", `${res.message} [${res.status}] [${res.error}]`);
        });
    });
}

const changeStudent = (student, fetchStudents, setData, newData, setEditingId) => {
    student.name = student.name.trim();
    student.email = student.email.trim();
    editStudent(student).then(() => {
        successNotification("Student changed", `Student ${student.name} with 
        email: ${student.email} and 
        Id: ${student.id} was changed`);
        setData(newData);
        setEditingId('');
        fetchStudents();
    }).catch(err => {
        err.response.json().then(res => {
            errorNotification("There was an issue", `${res.message} [${res.status}] [${res.error}]`);
        });
    });
}

const EditableCell = ({
    editing,
    dataIndex,
    title,
    inputType,
    record,
    index,
    children,
    ...restProps
}) => {
    const inputNode = inputType === 'gender' ?
        <Select placeholder="Gender"
            options={[
                {
                    value: 'MALE',
                    label: 'MALE',
                },
                {
                    value: 'FEMALE',
                    label: 'FEMALE',
                },
                {
                    value: 'OTHER',
                    label: 'OTHER',
                }
            ]}>
        </Select> :
        <Input />;
    return (
        <td {...restProps}>
            {editing ? (
                <Form.Item
                    name={dataIndex}
                    style={{
                        margin: 0,
                    }}
                    rules={[
                        {
                            required: true,
                            message: `Please Input ${title}!`,
                        },
                    ]}
                >
                    {inputNode}
                </Form.Item>
            ) : (
                children
            )}
        </td>
    );
};

const StudentsTable = ({ students, fetchStudents }) => {
    const [form] = Form.useForm();
    const [data, setData] = useState(students);
    const [editingId, setEditingId] = useState('');

    useEffect(() => {
        setData(students);
    }, [students])

    const isEditing = (record) => record.id === editingId;
    const edit = (record) => {
        form.setFieldsValue({
            // avatar: '',
            // id: '',
            name: '',
            email: '',
            gender: '',
            ...record,
        });
        setEditingId(record.id);
    };
    const cancel = () => {
        setEditingId('');
    };
    const save = async (id) => {
        try {

            const row = await form.validateFields();
            const newData = [...data];
            const index = newData.findIndex((item) => id === item.id);
            if (index > -1) {
                const item = newData[index];
                newData.splice(index, 1, {
                    ...item,
                    ...row,
                });

                if (item.name !== row.name ||
                    item.email !== row.email ||
                    item.gender !== row.gender) {
                    changeStudent(newData[index], fetchStudents, setData, newData, setEditingId);
                }

            } else {
                newData.push(row);
                setData(newData);
                setEditingId('');
            }
        } catch (errInfo) {
            console.log('Validate Failed:', errInfo);
            errInfo.response.json().then(res => {
                errorNotification("There was an issue", `${res.message} [${res.status}] [${res.error}]`);
            })
        }
    }
    const columns = [
        {
            title: '',
            dataIndex: 'avatar',
            key: 'avatar',
            render: (text, record) => <TheAvatar name={record.name} />
        },
        {
            title: 'Id',
            dataIndex: 'id',
            key: 'id',
        },
        {
            title: 'Name',
            dataIndex: 'name',
            key: 'name',
            editable: true
        },
        {
            title: 'Email',
            dataIndex: 'email',
            key: 'email',
            editable: true,
        },
        {
            title: 'Gender',
            dataIndex: 'gender',
            key: 'gender',
            editable: true,
        },
        {
            title: 'Action',
            dataIndex: 'action',
            key: 'action',
            render: (_, record) => {
                const editable = isEditing(record);
                return editable ? (
                    <span>
                        <Typography.Link
                            onClick={() => save(record.id)}
                            style={{
                                marginRight: 8,
                            }}
                        >
                            Save
                        </Typography.Link>

                        <Typography.Link>
                            <Popconfirm title="Sure to cancel?" onConfirm={cancel}>
                                <>Cancel</>
                            </Popconfirm>
                        </Typography.Link>
                    </span>
                ) : (
                    <Radio.Group
                        optionType="button"
                    >
                        <Popconfirm
                            placement="topLeft"
                            title={`Are you sure to delete ${record.name}?`}
                            onConfirm={() => { removeStudent(record.id, fetchStudents) }}
                            okText="Yes"
                            cancelText="No"
                        >
                            <Radio.Button value="delete">Delete</Radio.Button>
                        </Popconfirm>
                        <Popconfirm
                            placement="topLeft"
                            title={`Are you sure to change data for ${record.name}?`}
                            onConfirm={() => edit(record)}
                            okText="Yes"
                            cancelText="No"
                            disabled={editingId !== ''}
                        >
                            <Radio.Button value="edit">Edit</Radio.Button>
                        </Popconfirm>
                    </Radio.Group>

                );
            },
        },
    ];

    const mergedColumns = columns.map((col) => {
        if (!col.editable) {
            return col;
        }
        return {
            ...col,
            onCell: (record) => ({
                record,
                inputType: col.dataIndex === 'gender' ? 'gender' : 'text',
                dataIndex: col.dataIndex,
                title: col.title,
                editing: isEditing(record),
            }),
        };
    });
    return <Form form={form} component={false}>
        <Table
            rowKey={(record) => record.id}
            components={{
                body: {
                    cell: EditableCell,
                },
            }}

            dataSource={data}
            columns={mergedColumns}
            rowClassName="editable-row"
            pagination={{ pageSize: 50 }}
            scroll={{ y: 560 }}
            bordered
            title={() =>
                <div style={{ textAlign: 'right' }}>
                    <Tag
                    >Number of students
                    </Tag>
                    <Badge
                        className="site-badge-count-109"
                        count={data.length} />
                </div>
            }
        />
    </Form>
};
export default StudentsTable;