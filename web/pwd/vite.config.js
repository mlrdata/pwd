import {
	fileURLToPath,
	URL
} from 'node:url'

import {
	defineConfig
} from 'vite'
import vue from '@vitejs/plugin-vue'

import path from "path"

// https://vitejs.dev/config/
export default defineConfig({
	build: {
	        chunkSizeWarningLimit: 2000 // 增加到 1000 KiB
	    },
	plugins: [
		vue(),
	],
	base: './',
	resolve: {
		alias: {
			'@': fileURLToPath(new URL('./src', import.meta.url))
		},
	},
})