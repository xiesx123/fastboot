import { execSync } from 'child_process';
import fsExtra from 'fs-extra';
const { copySync } = fsExtra;

// 输出文件夹
const outDir = 'D:/Projects/fast/fast-boot/docs';

// 构建文档
console.log('📦 构建文档...');
execSync(`vitepress build --base /FastBoot/ --outDir "${outDir}"`, { stdio: 'inherit' });

// 拷贝图标
// console.log('🖼️  拷贝图片...');
// copySync('images', `${outDir}/images`);

console.log('✅ 打包完成');
