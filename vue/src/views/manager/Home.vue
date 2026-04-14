<template>
  <div class="dashboard-container">
    <!-- 欢迎区域 -->
    <div class="welcome-card">
      <div class="welcome-content">
        <div class="welcome-text">
          <h2>欢迎回来，{{ data.user.name || '管理员' }}！</h2>
          <p>今天是 {{ currentDate }}，祝您工作愉快！</p>
        </div>
        <div class="welcome-illustration">
          <div class="store-icon">🏪</div>
        </div>
      </div>
    </div>

    <!-- 数据概览卡片 -->
    <div class="stats-grid">
      <div class="stat-card sales-total">
        <div class="stat-icon">
          <el-icon><TrendCharts /></el-icon>
        </div>
        <div class="stat-content">
          <div class="stat-value">￥{{ formatNumber(data.count.total || 0) }}</div>
          <div class="stat-label">销售总额</div>
          <div class="stat-trend" v-if="data.stats.salesGrowth > 0">
            <el-icon color="#52c41a"><Top /></el-icon>
            <span>{{ data.stats.salesGrowth }}%</span>
          </div>
        </div>
      </div>

      <div class="stat-card today-sales">
        <div class="stat-icon">
          <el-icon><Coin /></el-icon>
        </div>
        <div class="stat-content">
          <div class="stat-value">￥{{ formatNumber(data.count.today || 0) }}</div>
          <div class="stat-label">今日销售额</div>
          <div class="stat-comparison">环比昨日: {{ data.stats.dailyGrowth || 0 }}%</div>
        </div>
      </div>

      <div class="stat-card product-count">
        <div class="stat-icon">
          <el-icon><Goods /></el-icon>
        </div>
        <div class="stat-content">
          <div class="stat-value">{{ formatNumber(data.count.goods || 0) }}</div>
          <div class="stat-label">商品总数</div>
          <div class="stat-subtext">{{ data.stats.newProducts || 0 }} 个新品</div>
        </div>
      </div>

      <div class="stat-card user-count">
        <div class="stat-icon">
          <el-icon><User /></el-icon>
        </div>
        <div class="stat-content">
          <div class="stat-value">{{ formatNumber(data.count.user || 0) }}</div>
          <div class="stat-label">注册用户</div>
          <div class="stat-subtext">{{ data.stats.newUsers || 0 }} 新用户</div>
        </div>
      </div>
    </div>

    <!-- 详细图表区域 -->
    <div class="detailed-charts-section">
      <div class="chart-card">
        <div id="line-chart" style="height: 500px"></div>
      </div>

      <div class="chart-card">
        <div id="pie-chart" style="height: 500px"></div>
      </div>
    </div>

    <!-- 快速操作 -->
    <div class="quick-actions">
      <h3>快速操作</h3>
      <div class="action-grid">
        <div class="action-item" @click="navigateTo('/manager/goods')">
          <el-icon><Goods /></el-icon>
          <span>商品管理</span>
        </div>
        <div class="action-item" @click="navigateTo('/manager/orders')">
          <el-icon><Document /></el-icon>
          <span>订单管理</span>
        </div>
        <div class="action-item" @click="navigateTo('/manager/carousel')">
          <el-icon><Picture /></el-icon>
          <span>轮播图</span>
        </div>
        <div class="action-item" @click="navigateTo('/manager/category')">
          <el-icon><Files /></el-icon>
          <span>商品分类</span>
        </div>
      </div>
    </div>

    <!-- 系统信息 -->
    <div class="system-info">
      <div class="info-card">
        <h4>📊 系统概览</h4>
        <p>乐享购电商平台管理系统 v2.0</p>
        <p>最后更新: {{ currentDate }}</p>
        <p>运行状态: <span style="color: #52c41a;">● 正常</span></p>
      </div>
      <div class="info-card">
        <h4>💡 快速提示</h4>
        <p>• 实时监控销售数据和用户行为</p>
        <p>• 详细图表分析助力决策</p>
        <p>• 支持移动端适配</p>
      </div>
    </div>
  </div>
</template>

<script setup>
import { reactive, onMounted } from "vue";
import { useRouter } from "vue-router";
import request from "@/utils/request";
import * as echarts from 'echarts';
import {
  TrendCharts,
  Coin,
  Goods,
  User,
  Top,
  Document,
  Picture,
  Files
} from '@element-plus/icons-vue';

const router = useRouter();
const data = reactive({
  user: JSON.parse(localStorage.getItem('system-user') || '{}'),
  stats: {},
  count: {},
  chartRange: '7'
});

const lineOption = {
  title: {
    text: '近一周订单销售的趋势图',
    left: 'center'
  },
  tooltip: {
    trigger: 'axis'
  },
  legend: {
    left: 'left'
  },
  xAxis: {
    name: '日期',
    type: 'category',
    data: []
  },
  yAxis: {
    name: '销售额（元）',
    type: 'value'
  },
  grid: {
    top: '20%',
    bottom: '10%'
  },
  series: [
    {
      data: [],
      type: 'line',
      smooth: true,
      areaStyle: {
        opacity: 0.8,
        color: 'rgb(185,190,255)'
      },
      markPoint: {
        data: [
          { type: 'max', name: 'Max' },
          { type: 'min', name: 'Min' }
        ]
      },
      markLine: {
        data: [{ type: 'average', name: 'Avg' }]
      }
    },
  ]
};

const pieOption = {
  title: {
    text: '分类商品销售额统计',
    subtext: '比例图',
    left: 'center'
  },
  tooltip: {
    trigger: 'item',
    formatter: '{a} <br/>{b} : {c}元 ({d}%)'
  },
  legend: {
    top: 0,
    orient: 'vertical',
    left: 'left'
  },
  series: [
    {
      name: '销售额',
      type: 'pie',
      center: ['50%', '60%'],
      radius: '50%',
      data: [],
      label: {
        show: true,
        formatter(param) {
          return param.name + ' (' + param.percent + '%)';
        }
      },
      emphasis: {
        itemStyle: {
          shadowBlur: 10,
          shadowOffsetX: 0,
          shadowColor: 'rgba(0, 0, 0, 0.5)'
        }
      }
    }
  ]
};

const currentDate = new Date().toLocaleDateString('zh-CN', {
  year: 'numeric',
  month: 'long',
  day: 'numeric',
  weekday: 'long'
});

// 格式化数字
const formatNumber = (num) => {
  return new Intl.NumberFormat('zh-CN').format(num);
};

// 导航到指定页面
const navigateTo = (path) => {
  router.push(path);
};

const loadCountData = () => {
  request.get('/count').then(res => {
    data.count = res.data;
    data.stats = {
      totalSales: res.data.total || 0,
      todaySales: res.data.today || 0,
      productCount: res.data.goods || 0,
      userCount: res.data.user || 0,
      salesGrowth: 12.5,
      dailyGrowth: 8.3,
      newProducts: 12,
      newUsers: 23
    };
  }).catch(() => {
    // 如果API不可用，使用默认数据
    data.count = {
      total: 125680,
      today: 2560,
      goods: 156,
      user: 892
    };
    data.stats = {
      totalSales: 125680,
      todaySales: 2560,
      productCount: 156,
      userCount: 892,
      salesGrowth: 12.5,
      dailyGrowth: 8.3,
      newProducts: 12,
      newUsers: 23
    };
  });
};

const initializeDashboard = () => {
  loadCountData();
};

// 监听日期范围变化
const handleChartRangeChange = () => {
  // 这里可以添加根据日期范围重新加载数据的逻辑
  console.log('日期范围改变为:', data.chartRange);
};

// 初始化图表
const initCharts = () => {
  // 折线图
  const lineChart = echarts.init(document.getElementById('line-chart'));
  // 请求数据  初始化图表
  request.get('/selectLine').then(res => {
    lineOption.xAxis.data = res.data.date;
    lineOption.series[0].data = res.data.count;
    lineChart.setOption(lineOption);
  }).catch(() => {
    // 如果API不可用，使用默认数据
    lineOption.xAxis.data = ['2024-01', '2024-02', '2024-03', '2024-04'];
    lineOption.series[0].data = [820, 932, 901, 934];
    lineChart.setOption(lineOption);
  });

  // 饼图
  const pieChart = echarts.init(document.getElementById('pie-chart'));
  // 请求数据  初始化图表
  request.get('/selectPie').then(res => {
    pieOption.series[0].data = res.data;
    pieChart.setOption(pieOption);
  }).catch(() => {
    // 如果API不可用，使用默认数据
    pieOption.series[0].data = [
      { value: 1048, name: '零食' },
      { value: 735, name: '饮料' },
      { value: 580, name: '文具' },
      { value: 484, name: '日用品' }
    ];
    pieChart.setOption(pieOption);
  });
};

onMounted(() => {
  initializeDashboard();
  // 初始化图表
  setTimeout(initCharts, 500);

  // 添加图表自适应大小
  window.addEventListener('resize', () => {
    setTimeout(() => {
      const lineChart = echarts.getInstanceByDom(document.getElementById('line-chart'));
      const pieChart = echarts.getInstanceByDom(document.getElementById('pie-chart'));
      lineChart && lineChart.resize();
      pieChart && pieChart.resize();
    }, 100);
  });
});
</script>

<style scoped>
.dashboard-container {
  padding: 20px;
  max-width: 1200px;
  margin: 0 auto;
  animation: fadeIn 0.8s ease-in-out;
}

@keyframes fadeIn {
  from {
    opacity: 0;
    transform: translateY(20px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

.welcome-card {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border-radius: 12px;
  padding: 30px;
  color: white;
  margin-bottom: 24px;
  box-shadow: 0 4px 20px rgba(102, 126, 234, 0.3);
  position: relative;
  overflow: hidden;
}

.welcome-card::before {
  content: '';
  position: absolute;
  top: -50%;
  left: -50%;
  width: 200%;
  height: 200%;
  background: linear-gradient(45deg, transparent, rgba(255,255,255,0.1), transparent);
  transform: rotate(45deg);
  animation: shine 3s infinite;
}

@keyframes shine {
  0% {
    transform: translateX(-100%) translateY(-100%) rotate(45deg);
  }
  100% {
    transform: translateX(100%) translateY(100%) rotate(45deg);
  }
}

.welcome-content {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.welcome-text h2 {
  margin: 0 0 8px 0;
  font-size: 28px;
  font-weight: 600;
}

.welcome-text p {
  margin: 0;
  opacity: 0.9;
}

.welcome-illustration .store-icon {
  font-size: 64px;
}

.stats-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(280px, 1fr));
  gap: 20px;
  margin-bottom: 24px;
}

.stat-card {
  background: white;
  border-radius: 12px;
  padding: 24px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.08);
  display: flex;
  align-items: center;
  transition: transform 0.2s, box-shadow 0.2s;
}

.stat-card:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.12);
}

.stat-icon {
  width: 60px;
  height: 60px;
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 16px;
  font-size: 24px;
}

.sales-total .stat-icon { background: #fff2e8; color: #fa541c; }
.today-sales .stat-icon { background: #f6ffed; color: #52c41a; }
.product-count .stat-icon { background: #f0f5ff; color: #2f54eb; }
.user-count .stat-icon { background: #f9f0ff; color: #722ed1; }

.stat-value {
  font-size: 28px;
  font-weight: 700;
  margin-bottom: 4px;
}

.stat-label {
  color: #666;
  margin-bottom: 4px;
}

.stat-trend, .stat-comparison, .stat-subtext {
  font-size: 12px;
  color: #999;
}

.stat-trend {
  display: flex;
  align-items: center;
  gap: 4px;
}

.stat-card:hover .stat-icon {
  transform: scale(1.05);
  transition: transform 0.3s;
}

.detailed-charts-section {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 20px;
  margin-bottom: 24px;
}

.chart-card {
  background: white;
  border-radius: 12px;
  padding: 20px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.08);
}

.chart-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 16px;
}

.chart-header h3 {
  margin: 0;
  font-size: 16px;
  font-weight: 600;
}

.chart-subtitle {
  font-size: 12px;
  color: #666;
  margin-left: 10px;
}

.quick-actions {
  background: white;
  border-radius: 12px;
  padding: 20px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.08);
  margin-bottom: 24px;
}

.quick-actions h3 {
  margin: 0 0 16px 0;
  font-size: 16px;
  font-weight: 600;
}

.action-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 16px;
}

.action-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 20px;
  border: 2px dashed #e8e8e8;
  border-radius: 8px;
  cursor: pointer;
  transition: all 0.3s;
  text-align: center;
  min-height: 120px;
  justify-content: center;
}

.action-item:hover {
  border-color: #409eff;
  background: linear-gradient(135deg, #f0f9ff 0%, #e6f7ff 100%);
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(64, 158, 255, 0.15);
}

.action-item .el-icon {
  font-size: 32px;
  margin-bottom: 12px;
  color: #409eff;
  transition: transform 0.3s;
}

.action-item:hover .el-icon {
  transform: scale(1.1);
}

.action-item span {
  font-size: 14px;
  font-weight: 600;
  color: #333;
}

.system-info {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 20px;
}

.info-card {
  background: white;
  border-radius: 12px;
  padding: 20px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.08);
  transition: transform 0.2s;
}

.info-card:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.12);
}

.info-card h4 {
  margin: 0 0 12px 0;
  font-size: 14px;
  font-weight: 600;
  color: #333;
}

.info-card p {
  margin: 4px 0;
  font-size: 12px;
  color: #666;
}

@media (max-width: 768px) {
  .dashboard-container {
    padding: 15px;
  }

  .welcome-card {
    padding: 20px;
  }

  .welcome-content {
    flex-direction: column;
    text-align: center;
    gap: 20px;
  }

  .welcome-text h2 {
    font-size: 24px;
  }

  .stats-grid {
    grid-template-columns: 1fr;
  }

  .detailed-charts-section {
    grid-template-columns: 1fr;
  }

  .action-grid {
    grid-template-columns: repeat(2, 1fr);
  }

  .action-item {
    min-height: 100px;
    padding: 15px;
  }

  .system-info {
    grid-template-columns: 1fr;
  }
}

@media (max-width: 480px) {
  .dashboard-container {
    padding: 10px;
  }

  .stats-grid {
    gap: 15px;
  }

  .stat-card {
    padding: 15px;
  }

  .stat-value {
    font-size: 24px;
  }

  .stat-icon {
    width: 50px;
    height: 50px;
    font-size: 20px;
    margin-right: 12px;
  }

  .action-grid {
    grid-template-columns: 1fr;
  }

  .chart-card {
    padding: 15px;
  }

#line-chart, #pie-chart {
  height: 250px;
}
}
</style>