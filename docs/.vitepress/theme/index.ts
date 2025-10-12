import type { Theme as ThemeConfig } from 'vitepress'
import { inBrowser, useData, useRoute } from "vitepress";
import DefaultTheme from "vitepress/theme";
import { h, type Plugin } from 'vue'
// readabilities
import type { Options } from '@nolebase/vitepress-plugin-enhanced-readabilities/client'
import { NolebaseEnhancedReadabilitiesMenu, NolebaseEnhancedReadabilitiesPlugin, NolebaseEnhancedReadabilitiesScreenMenu } from '@nolebase/vitepress-plugin-enhanced-readabilities/client'
import '@nolebase/vitepress-plugin-enhanced-readabilities/client/style.css'
// page-properties
import { NolebasePagePropertiesPlugin } from '@nolebase/vitepress-plugin-page-properties'
import '@nolebase/vitepress-plugin-page-properties/client/style.css'
// back-to-top
import backToTop from "vitepress-plugin-back-to-top";
import "vitepress-plugin-back-to-top/dist/style.css";
// codeblocks-fold
import codeblocksFold from 'vitepress-plugin-codeblocks-fold'; 
import 'vitepress-plugin-codeblocks-fold/style/index.css';
// analytics
import googleAnalytics from "vitepress-plugin-google-analytics";
// group-icons
import "virtual:group-icons.css";
// viewer
import imageViewer from "vitepress-plugin-image-viewer";
import 'viewerjs/dist/viewer.min.css';
// nprogress
import nprogress from "vitepress-plugin-nprogress";
import "vitepress-plugin-nprogress/lib/css/index.css";
// tabs
import { enhanceAppWithTabs } from "vitepress-plugin-tabs/client";
// vercount
import { useVisitData } from "./vercount";// @ts-ignore
// 自定义主题
export const Theme: ThemeConfig = {
  extends: DefaultTheme,
  Layout: () => {
    const props: Record<string, any> = {}
    // 获取 frontmatter 添加自定义样式
    const { frontmatter } = useData()
    if (frontmatter.value?.layoutClass) {
      props.class = frontmatter.value.layoutClass
    }
    // 注入插槽
    return h(DefaultTheme.Layout, props, {
      // 为较宽的屏幕的导航栏添加阅读增强菜单
      'nav-bar-content-after': () => h(NolebaseEnhancedReadabilitiesMenu),
      // 为较窄的屏幕（通常是小于 iPad Mini）添加阅读增强菜单
      'nav-screen-content-after': () => h(NolebaseEnhancedReadabilitiesScreenMenu),
    }); 
  },
  // 使用注入插槽的包装组件覆盖
  enhanceApp(ctx) {
    // 统计访问
    if (inBrowser) {
      // 路由加载完成，在加载页面组件后（在更新页面组件之前）调用。
      ctx.router.onAfterPageLoad = (fullPath: string) => {
        console.debug(fullPath);
        // 调用统计访问接口hooks
        useVisitData();
      };
    }
  },
}

// 默认主题
export default {
  extends: Theme,
  // Layout:LayoutSideAds,
  enhanceApp(ctx) {
    ctx.app.use(NolebaseEnhancedReadabilitiesPlugin, {spotlight: {} as Options})// 阅读增强
    ctx.app.use(NolebasePagePropertiesPlugin<{progress: number}>() as Plugin, { properties: { 'zh': [{ key: 'wordCount', type: 'dynamic', title: '字数', options: { type: 'wordsCount' } }, { key: 'readingTime', type: 'dynamic', title: '阅读', options: { type: 'readingTime', dateFnsLocaleName: 'zhCN' } }], "en": [{ key: 'wordCount', type: 'dynamic', title: 'Word count', options: { type: 'wordsCount' } }, { key: 'readingTime', type: 'dynamic', title: 'Reading time', options: { type: 'readingTime', dateFnsLocaleName: 'enUS' } }] } }); // 页面属性
    backToTop(); // back-to-top
    enhanceAppWithTabs(ctx.app); //tabs
    googleAnalytics({ id: "G-K1B7H0K4Z5" }); //analytics
    nprogress(ctx); //nprogress
  },
  setup() {
    // 获取数据
    const { frontmatter, isDark } = useData();
    // 获取路由
    const route = useRoute();

    // 代码块折叠
    codeblocksFold({route, frontmatter});

    // 图片预览
    const imageViewerProps = {
      button: false, // 右上角关闭按钮
      fullscreen: true, //全屏
      loading: true, // 加载框
      navbar: true, // 底部导航
      toolbar: true, // 工具栏
    };
    imageViewer(route, ".vp-doc", imageViewerProps);
  },
}
