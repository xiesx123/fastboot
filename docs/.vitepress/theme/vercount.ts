/**
 * 网站访问量统计
 */
export function useVisitData() {
  const script = document.createElement('script')
  script.defer = true
  script.async = true
  script.src = 'https://events.vercount.one/js'
  // script.src = '//busuanzi.ibruce.info/busuanzi/2.3/busuanzi.pure.mini.js'
  document.head.appendChild(script)
}
  
export default useVisitData