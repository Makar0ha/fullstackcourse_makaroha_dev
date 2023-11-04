import React from "react";
import { Popconfirm, Radio, Typography } from "antd";
import TheAvatar from "./TheAvatar";

export const columns = (fetchStudents, removeStudent, editingId, isEditing, save, edit, cancel) => [
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