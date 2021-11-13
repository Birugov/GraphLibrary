# GraphLibrary
Библиотка с представляниями для визуального отображения статистических данных

## LinearGraph - линейный граф, представляющий собой ломаную линию соединяющую ключевые точки с данными

![alt text](https://github.com/Birugov/GraphLibrary/blob/master/photo_2021-11-13_01-45-31.jpg)
### Пример использования
---
```
override fun onCreateView( inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {
        dashboardViewModel =
            ViewModelProvider(this).get(DashboardViewModel::class.java)

        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        val root: View = binding.root
        linearGraph = binding.linearGraph
        calculateBtn = binding.calculateBtn
        initPoints()

        val textView: LinearGraph = binding.linearGraph
        calculate()
        return root
    }

    private fun initPoints(){

        val randomSize = Random.nextInt(10)
        val arrayList:ArrayList<Pair<Float, Float>> = ArrayList()
        for (i in 0..randomSize){
            arrayList.add(Pair(Random.nextFloat()*5f, Random.nextFloat()*5f))
        }
        linearGraph.setCoordinatePoints(arrayList)
    }

    private fun calculate(){
        calculateBtn.setOnClickListener {
            initPoints()
            }
    }
 ```
 #### setCoordinatePoints(ArrayList<Pair<Float,Float>>)
 Данная функция нужна для передачи данных в виде пары значений Float для осей X и Y соответственно.
 Внутри у этого метода находятся функции сортировки значений по оси X, метод View.invalidate(), который грубо говоря перерисует ваш граф 
 и функция определяющая максимальные значения для осей в предоставленном списке для их правильной отрисовки
 ```
 fun setCoordinatePoints(coordinates: ArrayList<Pair<Float, Float>>) {
        this.coordinates = sortXCoordinates(coordinates)
        getMaxCoordinateValues()
        invalidate()
    }
 ```
#### XML-представление
```
<com.example.myapplication.LinearGraph
            android:id="@+id/linearGraph"
            android:layout_width="match_parent"
            android:layout_height="match_parent"
            app:layout_constraintBottom_toBottomOf="parent"
            app:layout_constraintEnd_toEndOf="parent"
            app:layout_constraintStart_toStartOf="parent"
            app:layout_constraintTop_toTopOf="parent"
            app:pointRadius="3"
            app:drawGraduations="true"
            app:drawGrids="true"
            app:gradientEndColor="@color/endColor"
            app:gradientStartColor="#fef08c"
            app:graduationColor="@color/colorPrimaryDark"
            app:graphType="START_AT_LEFT"
            app:gridColor="#fef08c"
            app:lineColor="#f8cb7a"
            app:pathWidth="2"/>
```

#####  Как понятно из названия, стартовый и конечные цвета градиента, заполняющие граф
  
```
app:gradientEndColor="@color/endColor"
app:gradientStartColor="#fef08c"
```

#####  Отображение и цвет чисел идущих вдоль осей графа
  
  ```
  app:drawGraduations="true"
  app:graduationColor="@color/colorPrimaryDark"
  
  ```
##### Тип графа, откуда будет начинаться отсчет
```
app:graphType="START_AT_LEFT"
```

##### Отрисовка и цвет осей и клеточного фона
```
app:drawGrids="true"
app:gridColor="#fef08c"
```
##### Толщина ломаной 
```
app:pathWidth="2"
```
## ChartGraph-график представляющий собой гстограмму
![alt text](https://github.com/Birugov/GraphLibrary/blob/master/photo_2021-11-13_01-46-49.jpg)

### Пример использования 
```
class NotificationsFragment : Fragment() {

    private lateinit var notificationsViewModel: NotificationsViewModel
    private var _binding: FragmentNotificationsBinding? = null

    private lateinit var chartChartGraph: ChartGraph
    private lateinit var generateBtn:MaterialButton

    private val binding get() = _binding!!

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        notificationsViewModel =
            ViewModelProvider(this).get(NotificationsViewModel::class.java)

        _binding = FragmentNotificationsBinding.inflate(inflater, container, false)
        val root: View = binding.root
        generateBtn = binding.generateDataBtn
        chartChartGraph =  binding.chartGraph

        generate()
        return root
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun generate(){
        generateBtn.setOnClickListener {
            generateData()
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun generateData(){
        val randomSize = Random.nextInt(10)
        val list:ArrayList<BarSeries> = ArrayList()
        for (i in 0..randomSize){
            val value = Random.nextFloat()
            list.add(BarSeries("label$i", value))
        }
        chartChartGraph.setSeries(list)

    }
```
#### XML-представление
```
 <com.example.graphs_views.ChartGraph
            android:id="@+id/chartGraph"
            android:layout_width="match_parent"
            android:layout_height="match_parent"
            android:layout_marginBottom="20dp"
            app:layout_constraintBottom_toBottomOf="parent"
            app:layout_constraintEnd_toEndOf="parent"
            app:layout_constraintStart_toStartOf="parent"
            app:layout_constraintTop_toTopOf="parent">
        </com.example.graphs_views.ChartGraph>
```

#### ChartGraph.setSeries(ArrayList<BarSeries>)
Данная функция сетит данные для отображения в виде объекта BarSeries

```
class BarSeries (
    val label: String, //название
    val value: Float //значение
        )
```

```
@RequiresApi(Build.VERSION_CODES.N)
    fun setSeries(barSeries: ArrayList<BarSeries>){
        this.series = barSeries
        val label = series.stream().max(Comparator.comparingInt { x->x.label.length }).get().label
        xLabelWidth = xLabelPainter.measureText(label)
        invalidate()
    }
```
Также функция обрабатывает названия элементов выбранных для отображения какой-либо статистики и перерисовывает представление как и первый граф
