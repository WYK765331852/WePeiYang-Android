package com.twtstudio.retrox.schedule.view;

import android.databinding.ObservableArrayList;

import com.kelin.mvvmlight.base.ViewModel;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;
import com.twtstudio.retrox.schedule.BR;
import com.twtstudio.retrox.schedule.R;
import com.twtstudio.retrox.schedule.model.ClassTable;
import com.twtstudio.retrox.schedule.model.ClassTableProvider;
import com.twtstudio.retrox.schedule.model.CourseHelper;

import java.util.List;

import me.tatarka.bindingcollectionadapter.ItemViewSelector;
import me.tatarka.bindingcollectionadapter.itemviews.ItemViewClassSelector;

/**
 * Created by zhangyulong on 7/3/17.
 */

public class ScheduleNewViewModel {
    private RxAppCompatActivity rxActivity;

    public final ObservableArrayList<ViewModel> items = new ObservableArrayList<>();
    public final ItemViewSelector itemView = ItemViewClassSelector.builder()
            .put(SelectedDateInfoViewModel.class, BR.viewModel, R.layout.item_selected_date)
            .put(SelectedCoursesInfoViewModel.class, BR.viewModel, R.layout.item_selected_courses)
            .build();

    public final CourseHelper courseHelper = new CourseHelper();

    public ScheduleNewViewModel(RxAppCompatActivity rxActivity, CalendarDay calendarDay) {
        this.rxActivity = rxActivity;
        initData(calendarDay);
    }

    public void initData(CalendarDay calendarDay) {
        ClassTableProvider.init(rxActivity)
                .registerAction2(this::processData)
                .getData(calendarDay);
    }


    private void processData(ClassTable classTable,CalendarDay calendarDay) {
        items.clear();
        courseHelper.setCalendar(calendarDay);
        items.add(new SelectedDateInfoViewModel(calendarDay));
        List<ClassTable.Data.Course> courseList = courseHelper.getTodayCourses(classTable, true);
        for (int i = courseList.size() - 1; i >= 0; i--) {
                //去除后面结尾的 "无" 空课程
                if (courseList.get(i).coursename.equals("无")) {
                    courseList.remove(i);
                } else {
                    break;
                }
            }
        for (ClassTable.Data.Course course : courseList) {
            if (!course.coursename.equals("无"))
                items.add(new SelectedCoursesInfoViewModel(course, rxActivity));
        }
    }

}
