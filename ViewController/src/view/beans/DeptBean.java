package view.beans;


import java.math.BigDecimal;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import oracle.adf.model.BindingContext;
import oracle.adf.model.binding.DCBindingContainer;
import oracle.adf.model.binding.DCIteratorBinding;
import oracle.adf.view.rich.component.rich.data.RichTable;

import oracle.adf.view.rich.component.rich.input.RichInputText;

import oracle.adfinternal.view.faces.model.binding.FacesCtrlHierBinding;

import oracle.binding.AttributeBinding;
import oracle.binding.BindingContainer;
import oracle.binding.OperationBinding;

import oracle.jbo.Key;
import oracle.jbo.Row;

import oracle.jbo.RowSetIterator;

import org.apache.myfaces.trinidad.model.RowKeySet;
//version9
public class DeptBean {
    private RichTable empTab;
    private RichInputText depName;

    public DeptBean() {
    }


    //version8

    public String methodBinding() {
        // get the binding container
        BindingContainer bindings = BindingContext.getCurrent().getCurrentBindingsEntry();

        // get an ADF attributevalue from the ADF page definitions
        AttributeBinding attr = (AttributeBinding) bindings.getControlBinding("DepartmentName");
        attr.setInputValue("test");

        // get an Action or MethodAction
        OperationBinding method = bindings.getOperationBinding("dep_method");
        method.execute();
        OperationBinding method2 = bindings.getOperationBinding("dep_row_method");
        method2.execute();

        // Get the data from an ADF tree or table
        DCBindingContainer dcBindings = (DCBindingContainer) BindingContext.getCurrent().getCurrentBindingsEntry();
        FacesCtrlHierBinding treeData = (FacesCtrlHierBinding) bindings.getControlBinding("EmployeesView2");
        Row[] rows = treeData.getAllRowsInRange();
        for (Row row : rows) {
            String firstname = row.getAttribute("FirstName").toString();
            System.out.println(firstname);
        }
        return null;
    }


    public String method_withparameter() {
        BindingContainer bindings = BindingContext.getCurrent().getCurrentBindingsEntry();
        AttributeBinding attr = (AttributeBinding) bindings.getControlBinding("DepartmentName");
        OperationBinding method = bindings.getOperationBinding("dep_method_with_parameter");
        Map obParam = method.getParamsMap();
        obParam.put("p1", attr.getInputValue());
        obParam.put("p2", attr.getInputValue());
        method.execute();
        return null;
    }

    public String DeptRaise() {
        BindingContainer bc = BindingContext.getCurrent().getCurrentBindingsEntry();
        DCBindingContainer dcb = (DCBindingContainer) bc;
        DCIteratorBinding iter = (DCIteratorBinding) dcb.findIteratorBinding("EmployeesView2Iterator");
        Row[] allRows = iter.getAllRowsInRange();
        BigDecimal currSal;
        for (Row r : allRows) {
            currSal = (BigDecimal) r.getAttribute("Salary");
            r.setAttribute("Salary", currSal.multiply(new BigDecimal(1.05)).setScale(0, BigDecimal.ROUND_DOWN));
            System.out.println(currSal.multiply(new BigDecimal(1.05))
                                      .setScale(0, BigDecimal.ROUND_DOWN)
                                      .toString());
        }
        OperationBinding method = bc.getOperationBinding("Commit");
        method.execute();
        //AdfFacesContext.getCurrentInstance().addPartialTarget(getEmpTab());
        return null;
    }

    public void setEmpTab(RichTable empTab) {
        this.empTab = empTab;
    }

    public RichTable getEmpTab() {
        return empTab;
    }

    public String ProcessEmps() {
        RowKeySet selectedEmps = getEmpTab().getSelectedRowKeys();
        Iterator selIter = selectedEmps.iterator();
        BindingContainer bc = BindingContext.getCurrent().getCurrentBindingsEntry();
        DCBindingContainer dcb = (DCBindingContainer) bc;
        DCIteratorBinding empIter = (DCIteratorBinding) dcb.findIteratorBinding("EmployeesView2Iterator");
        RowSetIterator rsi = empIter.getRowSetIterator();
        Row curr = null;
        while (selIter.hasNext()) {
            Key key = (Key) ((List) selIter.next()).get(0);
            curr = rsi.getRow(key);
            System.out.println(curr.getAttribute("FirstName"));
            // process row
        }
        return null;
    }

    public String ShowMessage() {
        FacesContext fctx = FacesContext.getCurrentInstance();
        FacesMessage fm = new FacesMessage("General message");
        fm.setSeverity(FacesMessage.SEVERITY_WARN);
        fctx.addMessage(null, fm);
        return null;
    }

    public String messageAttachment() {
        FacesContext fctx = FacesContext.getCurrentInstance();
        FacesMessage fm = new FacesMessage(FacesMessage.SEVERITY_INFO, "Summary", "This is the detailed message");
        String depID = getDepName().getClientId(fctx);
        fctx.addMessage(depID, fm);
        return null;
    }

    public void setDepName(RichInputText depName) {
        this.depName = depName;
    }

    public RichInputText getDepName() {
        return depName;
    }
}
