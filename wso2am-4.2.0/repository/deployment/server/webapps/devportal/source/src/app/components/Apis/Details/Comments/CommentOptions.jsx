/*
 * Copyright (c) 2019, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 * WSO2 Inc. licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
import React from 'react';
import PropTypes from 'prop-types';
import Grid from '@material-ui/core/Grid';
import Button from '@material-ui/core/Button';
import { withStyles } from '@material-ui/core/styles';
import { FormattedMessage } from 'react-intl';
import AuthManager from '../../../../data/AuthManager';

const styles = theme => ({
    link: {
        color: theme.palette.getContrastText(theme.palette.primary.main),
        '& span.MuiButton-label': {
            color: theme.palette.primary.main,
            fontWeight: '400',
        },
        cursor: 'pointer',
    },
    time: {
        color: theme.palette.getContrastText(theme.palette.background.default),
        marginTop: theme.spacing(0.3),
    },
    verticalSpace: {
        display: 'flex',
        alignItems: 'center',
    },
    disable: {
        color: theme.palette.grey[200],
    },
    commentIcon: {
        color: theme.palette.getContrastText(theme.palette.background.default),
    },
    commentText: {
        color: theme.palette.getContrastText(theme.palette.background.default),
        marginTop: theme.spacing.unig,
        width: '100%',
        whiteSpace: 'pre-wrap',
        overflowWrap: 'break-word',
    },
    root: {
        marginTop: theme.spacing(2.5),
    },
    contentWrapper: {
        maxWidth: theme.custom.contentAreaWidth,
        paddingLeft: theme.spacing(2),
        paddingTop: theme.spacing.unig,
    },
});

/**
 * Component to display options of the comment
 * @class CommentOptions
 * @extends {React.Component}
 */
class CommentOptions extends React.Component {
    /**
     * Creates an instance of CommentAdd
     * @param {*} props properies passed by the parent element
     * @memberof CommentAdd
     */
    constructor(props) {
        super(props);
        this.state = {};
        this.showAddComment = this.showAddComment.bind(this);
        this.showEditComment = this.showEditComment.bind(this);
        this.handleClickOpen = this.handleClickOpen.bind(this);
    }

    /**
     * Shows the component to add a new comment
     * @param {any} index Index of comment in the array
     * @memberof CommentOptions
     */
    showAddComment(replyId) {
        const { showAddComment } = this.props;
        showAddComment(replyId);
    }

    /**
     * Shows the component to edit a comment
     * @param {any} index Index of comment in the array
     * @memberof Comment
     */
    showEditComment(index) {
        const { editIndex, showEditComment } = this.props;
        if (editIndex === -1) {
            showEditComment(index);
        }
    }

    /**
     * Shows the confimation dialog to delete a comment
     * @param {Object} comment Comment that has to be deleted
     * @memberof Comment
     */
    handleClickOpen(comment) {
        const { editIndex, handleClickOpen } = this.props;
        if (editIndex === -1) {
            handleClickOpen(comment);
        }
    }

    /**
     * Returns the date and time in a particular format
     * @param {any} timestamp Timestamp that has to be formatted
     * @memberof CommentOptions
     */
    displayDate(timestamp) {
        const localDate = new Date(timestamp);
        const localDateString = localDate.toLocaleDateString(undefined, {
            day: 'numeric',
            month: 'short',
            year: 'numeric',
        });
        const localTimeString = localDate.toLocaleTimeString(undefined, {
            hour: '2-digit',
            minute: '2-digit',
        });
        return localDateString + ' ' + localTimeString;
    }

    /**
     * Render method of the component
     * @returns {React.Component} Comment html component
     * @memberof CommentOptions
     */
    render() {
        const {
            classes, comment, editIndex, index, theme,
        } = this.props;
        const user = AuthManager.getUser();
        const username = user && user.name;
        const canDelete = (comment.createdBy === username) || user && user.isAdmin();
        return (
            <Grid container className={classes.verticalSpace} key={comment.id}>
                {/* only the comment owner or admin can delete a comment */}
                {canDelete && [
                        <Grid item key='key-delete'>
                            <Button
                                size='small'
                                className={editIndex === -1 ? classes.link : classes.disable}
                                onClick={() => this.handleClickOpen(comment)}
                                color='primary'
                                aria-label={'Delete comment ' + comment.content}
                            >
                                <FormattedMessage
                                    id='Apis.Details.Comments.CommentOptions.delete'
                                    defaultMessage='Delete'
                                />
                            </Button>
                        </Grid>,

                    ]}

                {comment.parentCommentId === null && AuthManager.getUser() && [
                    <Grid item key='key-reply'>
                        <Button
                            size='small'
                            className={classes.link}
                            onClick={() => this.showAddComment(comment.id)}
                            color='primary'
                            aria-label={'Reply to comment ' + comment.content}
                        >
                            <FormattedMessage id='Apis.Details.Comments.CommentOptions.reply' defaultMessage='Reply' />
                        </Button>
                    </Grid>,
                ]}

                {/* only the comment owner can modify the comment from the exact entry point */}
                {/* {comment.createdBy === AuthManager.getUser().name
                    && comment.entryPoint === 'APIStore' && [
                        <Grid item key='key-edit'>
                            <Typography
                                component='a'
                                className={editIndex === -1 ? classes.link : classes.disable}
                                onClick={() => this.showEditComment(index)}
                            >
                                <FormattedMessage
                                    id='Apis.Details.Comments.CommentOptions.edit'
                                    defaultMessage='Edit'
                                />
                            </Typography>
                        </Grid>,
                        <Grid item key='key-edit-verical-divider'>
                            <VerticalDivider height={15} />
                        </Grid>,
                    ]} */}
                {/* <Grid item className={classes.time}>
                    <Typography component='a' variant='caption'>
                        {this.displayDate(comment.createdTime)}
                    </Typography>
                </Grid>
                {/* {editIndex === index
                    ? null
                    : [
                        <Grid item key='key-category-vertical-divider'>
                            <VerticalDivider height={15} />
                        </Grid>,
                        <Grid item className={classes.time} key='key-category'>
                            <Typography component='a' variant='caption'>
                                {comment.category}
                            </Typography>
                        </Grid>,
                    ]}  */}
            </Grid>
        );
    }
}

CommentOptions.defaultProps = {
    showAddComment: null,
};

CommentOptions.propTypes = {
    classes: PropTypes.instanceOf(Object).isRequired,
    editIndex: PropTypes.number.isRequired,
    index: PropTypes.number.isRequired,
    comment: PropTypes.instanceOf(Object).isRequired,
    handleClickOpen: PropTypes.func.isRequired,
    showEditComment: PropTypes.func.isRequired,
    showAddComment: PropTypes.func,
};

export default withStyles(styles, { withTheme: true })(CommentOptions);
