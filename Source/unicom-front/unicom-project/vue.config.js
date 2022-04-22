const webpack = require('webpack');
module.exports = {
  // publicPath: './',//打包的配置
  assetsDir: 'static',
  outputDir: 'unicom',
  configureWebpack: {
    plugins: [
      new webpack.ProvidePlugin({
        $:"jquery",
        jQuery:"jquery",
        "windows.jQuery":"jquery"
      })
    ]
  },
  lintOnSave: true,
  devServer: {
    proxy: {
      '/unicom': {
        // target:'https://192.168.93.197:9443',
        // target:'https://192.168.93.186:9443',
        target:'http://192.168.90.59:9443',
        changeOrigin: true,
      }
    }
  }
};
