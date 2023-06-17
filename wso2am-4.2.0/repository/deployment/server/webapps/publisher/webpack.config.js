/**
 * Copyright (c) 2017, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 * WSO2 Inc. licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 *
 */
const MonacoWebpackPlugin = require('monaco-editor-webpack-plugin');
const path = require('path');
const { CleanWebpackPlugin } = require('clean-webpack-plugin');
const ESLintPlugin = require('eslint-webpack-plugin');
const webpack = require('webpack');
const { BundleAnalyzerPlugin } = require('webpack-bundle-analyzer');
const HtmlWebpackPlugin = require('html-webpack-plugin');
const { clientRoutingBypass, devServerBefore } = require('./services/dev_proxy/auth_login.js');

// When exporting a function, Current mode is received as the first arg to the function.
// https://github.com/webpack/webpack/issues/6460#issuecomment-364286147
module.exports = (env, argv) => {
    const isDevelopmentBuild = argv.mode === 'development';
    const isTestBuild = process.env && process.env.WSO2_UI_MOCKED === 'true';
    /**
     * Notes:
     *      - swaggerWorkerInit entry has being removed until we resolve
     *              https://github.com/wso2/product-apim/issues/10694 issue, need to change index.html too
     */
    const config = {
        entry: {
            index: './source/index.jsx',
            // swaggerWorkerInit: './source/src/app/webWorkers/swaggerWorkerInit.js',
        },
        output: {
            path: path.resolve(__dirname, 'site/public/dist'),
            filename: isDevelopmentBuild || isTestBuild ? '[name].bundle.js' : '[name].[contenthash].bundle.js',
            chunkFilename: isDevelopmentBuild || isTestBuild
                ? '[name].chunk.bundle.js' : '[name].[contenthash].bundle.js',
            publicPath: 'site/public/dist/',
            globalObject: 'this',
        },
        node: {
            fs: 'empty',
            net: 'empty', // To fix joi issue: https://github.com/hapijs/joi/issues/665#issuecomment-113713020
        },
        watch: false,
        watchOptions: {
            poll: 1000,
            ignored: ['files/**/*.js', 'node_modules'],
        },
        devtool: 'source-map', // Note*: Commented out the
        // source mapping (devtool) in case need to speed up the build time & reduce size
        /**
         * Webpack devserver configuration
         * Configured to open the browser with /publisher context, Keep builds in-memory, hot updated enabled
         * Overlay the error messages in the app,
         * and use proxy configs and `devServerBefore` to handle authentication requests.
         * For more info:
         *      https://webpack.js.org/configuration/dev-server/
         *      https://github.com/gaearon/react-hot-loader
        */
        devServer: {
            open: !isTestBuild,
            openPage: 'publisher',
            inline: true,
            hotOnly: !isTestBuild,
            hot: true,
            publicPath: '/site/public/dist/',
            writeToDisk: false,
            overlay: true,
            before: devServerBefore,
            proxy: {
                '/services/': {
                    target: 'https://localhost:9443/publisher',
                    secure: false,
                },
                '/api/am/publisher/v4/swagger.yaml': {
                    target: isTestBuild ? 'https://raw.githubusercontent.com/wso2/carbon-apimgt/master/components/apimgt/org.wso2.carbon.apimgt.rest.api.publisher.v1/src/main/resources/publisher-api.yaml' : 'https://localhost:9443/api/am/publisher/v4/swagger.yaml',
                    secure: false,
                    changeOrigin: true,
                    pathRewrite: { '^/api/am/publisher/v4/swagger.yaml': '' },
                },
                '/api/am/service-catalog/v1/oas.yaml': {
                    target: isTestBuild ? 'https://raw.githubusercontent.com/wso2/carbon-apimgt/master/components/apimgt/org.wso2.carbon.apimgt.rest.api.service.catalog/src/main/resources/service-catalog-api.yaml' : 'https://localhost:8081/api/am/service-catalog/v1/oas.yaml',
                    secure: false,
                    changeOrigin: true,
                    pathRewrite: { '^/api/am/service-catalog/v1/oas.yaml': '' },
                },
                '/api/am': {
                    target: isTestBuild ? 'http://localhost:4010' : 'https://localhost:9443',
                    // pathRewrite: { '^/api/am/publisher/v4/': '' },
                    secure: false,
                },
                '/publisher/services': {
                    target: 'https://localhost:9443',
                    secure: false,
                },
                '/publisher': {
                    bypass: clientRoutingBypass,
                },
            },
        },
        resolve: {
            alias: {
                AppData: path.resolve(__dirname, 'source/src/app/data/'),
                AppComponents: path.resolve(__dirname, 'source/src/app/components/'),
                OverrideData: path.resolve(__dirname, 'override/src/app/data/'),
                OverrideComponents: path.resolve(__dirname, 'override/src/app/components/'),
                AppTests: path.resolve(__dirname, 'source/Tests/'),
                'nimma/fallbacks': require.resolve('./node_modules/nimma/dist/legacy/cjs/fallbacks/index.js'), // nimma/* things Added because of spectral
                'nimma/legacy': require.resolve('./node_modules/nimma/dist/legacy/cjs/index.js'),
                nimma: require.resolve('./node_modules/nimma/dist/legacy/cjs/index.js'),
            },
            extensions: ['.js', '.jsx', '.ts', '.tsx'],
        },
        module: {
            rules: [
                {
                    test: /\.worker\.js$/,
                    use: { loader: 'worker-loader' },
                },
                {
                    test: /\.(js|jsx)$/,
                    exclude: /node_modules/,
                    use: [
                        {
                            loader: 'babel-loader',
                        },
                        {
                            loader: path.resolve('loader.js'),
                        },
                    ],
                },
                {
                    test: /\.tsx?$/,
                    use: [
                        {
                            loader: 'ts-loader',
                            options: {
                                onlyCompileBundledFiles: true,
                            },
                        },
                    ],
                },
                {
                    test: /\.css$/i,
                    use: ['style-loader', 'css-loader'],
                },
                {
                    test: /\.less$/,
                    use: [
                        {
                            loader: 'style-loader', // creates style nodes from JS strings
                        },
                        {
                            loader: 'css-loader', // translates CSS into CommonJS
                        },
                        {
                            loader: 'less-loader', // compiles Less to CSS
                        },
                    ],
                },
                {
                    test: /\.(png|jpe?g|gif|svg|eot|ttf|woff|woff2)$/i,
                    loader: 'url-loader',
                    options: {
                        limit: 8192,
                    },
                },
                // Until we migrate to webpack 5 https://github.com/jantimon/html-webpack-plugin/issues/1483 ~tmkb
                // This is added to generate the index.jsp from a hbs template file including the hashed bundle file
                {
                    test: /\.jsp\.hbs$/,
                    loader: 'underscore-template-loader',
                    query: {
                        engine: 'lodash',
                        interpolate: '\\{\\[(.+?)\\]\\}',
                        evaluate: '\\{%([\\s\\S]+?)%\\}',
                        escape: '\\{\\{(.+?)\\}\\}',
                    },
                },
            ],
        },
        externals: {
            userCustomThemes: 'userThemes', // Should use long names for preventing global scope JS variable conflicts
            MaterialIcons: 'MaterialIcons',
            Config: 'AppConfig',
            Settings: 'Settings',
        },
        plugins: [
            new MonacoWebpackPlugin({ languages: ['xml', 'json', 'yaml', 'markdown', 'javascript'], 
                features: ['!gotoSymbol'] }),
            new CleanWebpackPlugin(),
            new HtmlWebpackPlugin({
                inject: false,
                template: path.resolve(__dirname, 'site/public/pages/index.jsp.hbs'),
                filename: path.resolve(__dirname, 'site/public/pages/index.jsp'),
                minify: false, // Make this true to get exploded, formatted index.jsp file
            }),
            new ESLintPlugin({
                extensions: ['js', 'ts', 'jsx'],
                failOnError: true,
                quiet: true,
                exclude: 'node_modules',
            }),
            new webpack.ProgressPlugin((percentage, message, ...args) => {
                // e.g. Output each progress message directly to the console:
                const pres = Math.round(percentage * 100);
                if (pres % 20 === 0) console.info(`${pres}%`, message, ...args); // To reduce log lines
            }),
        ],
    };
    const isAnalysis = process.env && process.env.NODE_ENVS === 'analysis';
    if (isAnalysis) {
        config.plugins.push(new BundleAnalyzerPlugin());
    }
    return config;
};
