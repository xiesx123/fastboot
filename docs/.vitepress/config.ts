// vitepress
import { defineConfig, PageData } from 'vitepress'
// page properties
import {PageProperties, PagePropertiesMarkdownSection } from '@nolebase/vitepress-plugin-page-properties/vite'
const links: { url: string; lastmod: PageData['lastUpdated'], changefreq: string }[] = []
// mdit
import { align } from "@mdit/plugin-align";
import { icon, iconifyRender } from "@mdit/plugin-icon";
import { imgMark } from "@mdit/plugin-img-mark";
import { imgSize } from "@mdit/plugin-img-size";
import { tasklist } from "@mdit/plugin-tasklist";
// group-icons
import { groupIconMdPlugin, groupIconVitePlugin } from "vitepress-plugin-group-icons";
// tabs
import { tabsMarkdownPlugin } from "vitepress-plugin-tabs";

// refer https://vitepress.dev/reference/site-config for details
export default defineConfig({
  base: "/fastboot/",
  srcDir: "src",
  assetsDir: "static",
  lang: "zh-CN",
  appearance: "dark",
  title: "FastBoot",
  cleanUrls: true,
  metaChunk: true,
  sitemap: {
    hostname: 'https://xiesx123.github.io/fastboot/'
  },
  head: [
    ["link", { rel: "icon", type: "image/x-icon", href: "/fastboot/favicon.ico" }],
    ["link", { rel: "preload stylesheet", href: "/fastboot/style.Avo_XaLv.css" }],
    ["script", { src: "/fastboot/js/iconify-icon.min.js" }],
    ["meta", { property: "og:type", content: "website" }],
    ["meta", { property: "og:site_name", content: "FastBoot" }],
  ],
  themeConfig: {
    logo: { src: "/favicon.ico" },
    search: { provider: "local" },
    outline: "deep",
  },
  locales: {
    root: {
      label: "简体中文",
      lang: "zh",
      description: "灵活、高效、可扩展，专属视频剪辑配音工具箱，释放创作潜力.",
      head: [
        ["meta", { property: "og:image", content: "https://xiesx123.github.io/fastboot/images/FastBoot_zh.png" }],
      ],
      themeConfig: {
        darkModeSwitchLabel: "切换主题",
        outlineTitle: "页面目录",
        returnToTopLabel: "返回顶部",
        sidebarMenuLabel: "菜单",
        lastUpdated: { text: "最后更新" },
        docFooter: { prev: "上一篇", next: "下一篇" },
        notFound: { title: "页面未找到", quote: "哎呀，您好像迷失在网络的小胡同里啦，别着急，赶紧回头是岸！", linkText: "返回首页" },
        search: {
          provider: "local",
          options: {
            translations: {
              button: { buttonText: "搜索" },
              modal: {
                displayDetails: "1",
                resetButtonTitle: "清除查询条件",
                backButtonTitle: "1",
                noResultsText: "无法找到相关结果",
                footer: { selectText: "选择", navigateText: "切换", closeText: "关闭" },
              },
            },
          },
        },
        sidebar: [
          {
            text: "快速开始", link: "/quick-start" 
          },
          {
            text: "注解",
            items: [
              { text: "统一返回", link: "/core/advice" },
              { text: "事件总线", link: "/core/event" },
              { text: "全局异常", link: "/core/exception" },
              { text: "数据转换", link: "/core/json" },
              { text: "请求限流", link: "/core/limiter" },
              { text: "日志打印", link: "/core/logger" },
              { text: "数据签名", link: "/core/signature" },
              { text: "令牌认证", link: "/core/token" },
            ],
          },
          {
            text: "持久化",
            link: "/db/index",
            items: [
              { text: "QueryDsl", link: "/db/querydsl" },
              { text: "Spring Data Jdbc", link: "/db/spring_jdbc" },
              { text: "Spring Data Jpa", link: "/db/spring_data_jpa" },
            ],
          },
          {
            text: "拓展",
            collapsed: true,
            items: [
              { text: "异步增强", link: "/support/async" },
              { text: "网络请求", link: "/support/request" },
              { text: "重试机制", link: "/support/retry" },
              { text: "任务调度", link: "/support/scheduler" },
              { text: "数据效验", link: "/support/validate" },
            ],
          },
          // {
          //   text: "实验性",
          //   items: [
          //     { text: "执行器", link: "/support/license" },
          //     { text: "模板标签", link: "/support/taglib" },
          //     { text: "代码生成", link: "/support/generate" },
          //     { text: "授权许可", link: "/support/license" },
          //   ],
          // },
        ],
        socialLinks: [
          { icon: "github", link: "https://github.com/xiesx123/FastBoot" },
        ],
      },
    },
  },
  markdown: {
    image: { lazyLoading: true },
    config: (md) => {
      md.use(align);
      md.use(icon, { render: iconifyRender });
      md.use(imgMark);
      md.use(imgSize);
      md.use(tasklist);
      md.use(groupIconMdPlugin, { titleBar: { includeSnippet: true } });
      md.use(tabsMarkdownPlugin);
    },
  },
  vite: {
    ssr: {
      noExternal: ['@nolebase/*'],
    },
    plugins: [ 
      PageProperties() as any,
      PagePropertiesMarkdownSection({
        // excludes: ['index.md'],
        exclude: (id) => {
          return id.endsWith('index.md') || id.endsWith('team.md')
      }}),
      groupIconVitePlugin()
    ],
  },
  transformHtml: (_, id, { pageData }) => {
    const path = pageData.relativePath
      if (
        !/[\\/]404\.html$/.test(id) && 
        !/[\\/]empty\.html$/.test(id) && 
        !/^snippet\//.test(path)
      )
      links.push({
        url: pageData.relativePath.replace(/((^|\/)index)?\.md$/, '$2'),
        lastmod: pageData.lastUpdated,
        changefreq: 'always',
      })
  },
});
