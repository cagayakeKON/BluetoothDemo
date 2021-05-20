package com.cagayake.bluetoothgraphics.fragment

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.navigation.fragment.findNavController
import com.cagayake.bluetoothgraphics.databinding.FragmentGraphicsBinding
import com.cagayake.bluetoothgraphics.pojo.GraphicData
import com.cagayake.bluetoothgraphics.rxbus.RxBus
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import io.reactivex.Scheduler
import io.reactivex.schedulers.Schedulers
import java.time.LocalDateTime
import java.util.*
import kotlin.collections.ArrayList


class GraphicsFragment : Fragment() {
    private var _binding: FragmentGraphicsBinding? = null
    private val binding get() = _binding!!
    private val data:LinkedList<GraphicData> = LinkedList()

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentGraphicsBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("CheckResult")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        RxBus.toObservable(String::class.java).subscribe {
            data.add(GraphicData(LocalDateTime.now().second,it.toInt()))
            val dataEntry:ArrayList<Entry> = ArrayList()
            for (item in data){
                dataEntry.add(Entry(item.x,item.y))
            }
            val dataSet = LineDataSet(dataEntry,"Graphic");
            val lineData = LineData(dataSet);
            binding.lineChart.data = lineData
            binding.lineChart.invalidate()
        }
    }

    override fun onDestroyView() {

        super.onDestroyView()
        _binding = null
    }
}