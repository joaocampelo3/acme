import React from 'react';
import { makeStyles, useTheme } from '@material-ui/core/styles';
import HTMLRender from 'AppComponents/Shared/HTMLRender';

const useStyles = makeStyles(() => ({
    root: {
        paddingTop: 20,
        paddingBottom: 20,
    },
}));
/**
 * Renders parallax scroll section.
 * @param {JSON} props Parent pros.
 * @returns {JSX} rendered parallax scroll view.
 */
function Contact() {
    const classes = useStyles();
    const theme = useTheme();
    const { custom: { landingPage: { contact: { contactHTML } } } } = theme;
    return (
        <div className={classes.root}>
            <HTMLRender html={contactHTML} />
        </div>
    );
}

export default Contact;
