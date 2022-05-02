const chokidar = require('chokidar')
const merger = require('swagger-merger')

chokidar.watch('/data').on('all', (event, path) => {
  console.log(event, path)
  merger({ input: 'index.yml', output: '/output/swagger.yml' }).catch(error => console.error(error))
})
