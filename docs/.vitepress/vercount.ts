//  https://juejin.cn/post/7442554317052493850

/**
 * 网站访问量统计 hooks
 */
function useVisitData() {
    const script = document.createElement('script')
    script.defer = true
    script.async = true
  
    // 调用 Vercount 接口
    // script.src = 'https://cn.vercount.one/js'
    script.src = 'https://events.vercount.one/js'
  
    // 调用 不蒜子 接口（被注释掉了）
    // script.src = '//busuanzi.ibruce.info/busuanzi/2.3/busuanzi.pure.mini.js'
    document.head.appendChild(script)
  }
  
  export default useVisitData
  