/* 站点*/
export interface NavItem {
    /** 名称 */
    title: string;
    /** 图标 */
    icon?: string | { svg: string };
    /** 徽标 */
    badge?: | string | { text?: string; type?: "info" | "tip" | "warning" | "danger"; };
    /** 名称 */
    desc?: string;
    /** 链接 */
    link: string;
}

/** 导航 */
export interface NavList {
    title: string;
    items: NavItem[];
}
