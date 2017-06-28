package cl.deloitte.wsib;



import org.jdatepicker.impl.UtilDateModel;

import javax.swing.*;



/**
 * Created by jinostrozau on 2017-06-28.
 */
public class AcesViewerIssues {
    private JPanel panel1;

    public static void main(String[] args){
        new AcesViewerIssues();
    }

    public AcesViewerIssues() {

    }

    UtilDateModel model = new UtilDateModel();
    JDatePanelImpl datePanel = new JDatePanelImpl(model);
    JDatePickerImpl datePicker = new JDatePickerImpl(datePanel);

    frame.add(datePicker);
}
