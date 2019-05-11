package com.tamimi.sundos.restpos;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.tamimi.sundos.restpos.Models.Cashier;
import com.tamimi.sundos.restpos.Models.Cheque;
import com.tamimi.sundos.restpos.Models.CreditCard;
import com.tamimi.sundos.restpos.Models.ItemWithScreen;
import com.tamimi.sundos.restpos.Models.Money;
import com.tamimi.sundos.restpos.Models.OrderHeader;
import com.tamimi.sundos.restpos.Models.OrderTransactions;
import com.tamimi.sundos.restpos.Models.PayMethod;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.print.PrintHelper;

public class PayMethods extends AppCompatActivity {

    ImageView print;
    Button cash, creditCard, cheque, giftCard, credit, point, save;
    TextView tableNumber, check, date, remainingBalance, server, orderAmount, discount, subCharge, subTotal, tax, amountDue, deliveryCharge,
            totalDue, totalReceived, balance;

    DatabaseHandler mDHandler;
    Dialog dialog, dialog1;
    DecimalFormat twoDForm = new DecimalFormat("#.00");
    TextView focusedTextView;
    int flag = 0;
    int position1;
    ArrayList creditCardsName;
    ArrayAdapter<String> adapter;

    ArrayList<String> bankName, cardNumbers, chequeNambers, cardName, giftCardNumber, couponNumber, pointCardNumber,
            resiveCredit, resivePoint, resiveGift, resiveCheque;
    int position, countCridit = 0, countCheque = 0, countGift = 0, countCoupon = 0, countPoint = 0;

    Order obj;
    String orderType = "TakeAway";

    List<OrderTransactions> orderTransTemp = null;
    List<OrderHeader> orderHeaderTemp = null;
    String sectionNo, tableNo;

    ArrayList chequeListName;
    ArrayAdapter<String> adapter2;

    String mainBalance;
    double cashValue = 0.00;
    double creditCardValue = 0.00;
    double chequeValue = 0.00;
    double giftCardValue = 0.00;
    double creditValue = 0.00;
    double pointValue = 0.00;

    public static double cashValue1 = 0.00;
    public static double creditCardValue1 = 0.00;
    public static double chequeValue1 = 0.00;
    public static double giftCardValue1 = 0.00;
    public static double creditValue1 = 0.00;
    public static double pointValue1 = 0.00;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.pay_method);

        initialize();

        Date currentTimeAndDate = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        String today = convertToEnglish(df.format(currentTimeAndDate));

        date.setText(convertToEnglish(date.getText().toString() + " " + today));
        mDHandler = new DatabaseHandler(PayMethods.this);

        focusedTextView = null;
        obj = new Order();

        Bundle extras = getIntent().getExtras();
        if (extras != null) { // pay from dine in
            sectionNo = extras.getString("sectionNo");
            tableNo = extras.getString("tableNo");
            Log.e("lll", "" + sectionNo + " " + tableNo);

            orderTransTemp = mDHandler.getOrderTransactionsTemp(sectionNo, tableNo);
            orderHeaderTemp = mDHandler.getOrderHeaderTemp(sectionNo, tableNo);

            balance.setText(orderHeaderTemp.get(0).getAmountDue() + "");
            server.setText(orderHeaderTemp.get(0).getWaiter());
            discount.setText(orderHeaderTemp.get(0).getAllDiscount() + "");
            subTotal.setText(orderHeaderTemp.get(0).getSubTotal() + "");
            tax.setText(orderHeaderTemp.get(0).getTotalTax() + "");
            amountDue.setText(orderHeaderTemp.get(0).getAmountDue() + "");
            deliveryCharge.setText(orderHeaderTemp.get(0).getDeliveryCharge() + "");

            mainBalance = balance.getText().toString();
            remainingBalance.setText(getResources().getString(R.string.remaining_) + balance.getText().toString());
            check.setText(check.getText().toString() + " " + orderHeaderTemp.get(0).getSectionNO());
            tableNumber.setText(tableNumber.getText().toString() + " " + orderHeaderTemp.get(0).getTableNO());
            orderType = "Dine In";

        } else {  // pay from takeaway


            balance.setText(obj.getOrderHeaderObj().getAmountDue() + "");
            orderAmount.setText(obj.getOrderHeaderObj().getTotal() + "");
            discount.setText(obj.getOrderHeaderObj().getAllDiscount() + "");
            deliveryCharge.setText(obj.getOrderHeaderObj().getDeliveryCharge() + "");
            server.setText(obj.getOrderHeaderObj().getWaiter());
            subTotal.setText(obj.getOrderHeaderObj().getSubTotal() + "");
            tax.setText(obj.getOrderHeaderObj().getTotalTax() + "");
            amountDue.setText(obj.getOrderHeaderObj().getAmountDue() + "");


            mainBalance = convertToEnglish(balance.getText().toString());
            remainingBalance.setText(getResources().getString(R.string.remaining_) + balance.getText().toString());
            check.setText(check.getText().toString() + " -");
            tableNumber.setText(tableNumber.getText().toString() + " -");
        }

        cardNumbers = new ArrayList<String>();
        cardName = new ArrayList<String>();
        bankName = new ArrayList<String>();
        chequeNambers = new ArrayList<String>();
        giftCardNumber = new ArrayList<String>();
        couponNumber = new ArrayList<String>();
        pointCardNumber = new ArrayList<String>();
        resiveCredit = new ArrayList<String>();
        resivePoint = new ArrayList<String>();
        resiveCheque = new ArrayList<String>();
        resiveGift = new ArrayList<String>();

    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            switch (view.getId()) {
                case R.id.print:

                    break;

                case R.id.cash:
                    showCashDialog();
                    break;

                case R.id.credit_card:
                    showCreditCardDialog();

                    break;

                case R.id.cheque:
                    showChequeDialog();
                    break;

                case R.id.gift_card:
                    showGiftCardDialog();
                    break;

                case R.id.credit:
                    showCardDialog();
                    break;

                case R.id.point:
                    showPointDialog();
                    break;

                case R.id.save:
                    saveInDataBase();
                    break;


            }
        }
    };

    @SuppressLint("ClickableViewAccessibility")
    void showCashDialog() {
        dialog = new Dialog(PayMethods.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.cash_dialog);
        dialog.setCanceledOnTouchOutside(true);

//        Window window = dialog.getWindow();
//        window.setLayout(780, 460);
        final TextView balance = (TextView) dialog.findViewById(R.id.balance);
        final TextView received = (TextView) dialog.findViewById(R.id.receivedCash);
        final TextView cashMoney = (TextView) dialog.findViewById(R.id.cashMoney);
        final TableLayout tableLayout = (TableLayout) dialog.findViewById(R.id.money_categories);


        balance.setText(mainBalance);
        final ArrayList<Money> moneyList;
        moneyList = mDHandler.getAllMoneyCategory();

        received.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                received.setText("");
                cashMoney.setText("0.00");
                flag = 0;
                for (int i = 0; i < moneyList.size(); i++) {
                    TableRow tRaw = (TableRow) tableLayout.getChildAt(i);
                    TextView text = (TextView) tRaw.getChildAt(2);
                    text.setText("0");
                }

            }
        });

        for (int i = 0; i < moneyList.size(); i++) {
            position1 = i;

            final TableRow row = new TableRow(PayMethods.this);
            TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT);
            row.setLayoutParams(lp);

            TextView textView = new TextView(PayMethods.this);
            textView.setText(moneyList.get(i).getCatName());
            textView.setTag(moneyList.get(i).getCatValue());
            textView.setTextColor(ContextCompat.getColor(PayMethods.this, R.color.text_color));
            textView.setGravity(Gravity.CENTER);
            textView.setBackgroundColor(getResources().getColor(R.color.layer3));

            ImageView imageView = new ImageView(PayMethods.this);
            imageView.setImageDrawable(new BitmapDrawable(getResources(), moneyList.get(i).getPicture()));
            imageView.setBackgroundColor(getResources().getColor(R.color.layer3));

            TableRow.LayoutParams lp2 = new TableRow.LayoutParams(150, 50);
            lp2.setMargins(0, 5, 0, 0);
            textView.setLayoutParams(lp2);
            imageView.setLayoutParams(lp2);

            //--------------------------
            TextView textView1 = new TextView(PayMethods.this);
            textView1.setText("0");
            textView1.setTextColor(ContextCompat.getColor(PayMethods.this, R.color.layer3));
            textView1.setGravity(Gravity.CENTER);
            textView1.setBackgroundColor(getResources().getColor(R.color.layer3));
            TableRow.LayoutParams lp1 = new TableRow.LayoutParams(1, 1);
            lp1.setMargins(0, 5, 0, 0);
            textView1.setLayoutParams(lp1);
            //----------------------------

            final double catValue = moneyList.get(i).getCatValue();
            row.addView(imageView);
            row.addView(textView);
            row.addView(textView1);
            row.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    cashMoney.setText("" + (Double.parseDouble(cashMoney.getText().toString()) + catValue));
                    TextView t1 = (TextView) row.getChildAt(2);
                    textView1.setText("" + (Integer.parseInt(t1.getText().toString()) + 1));

                    Log.e("111", "11" + t1.getText().toString() + "*********");
                }
            });

            tableLayout.addView(row);
        }

        Button b1, b2, b3, b4, b5, b6, b7, b8, b9, b0, dot, save;
        b1 = (Button) dialog.findViewById(R.id.b1);
        b2 = (Button) dialog.findViewById(R.id.b2);
        b3 = (Button) dialog.findViewById(R.id.b3);
        b4 = (Button) dialog.findViewById(R.id.b4);
        b5 = (Button) dialog.findViewById(R.id.b5);
        b6 = (Button) dialog.findViewById(R.id.b6);
        b7 = (Button) dialog.findViewById(R.id.b7);
        b8 = (Button) dialog.findViewById(R.id.b8);
        b9 = (Button) dialog.findViewById(R.id.b9);
        b0 = (Button) dialog.findViewById(R.id.b0);
        dot = (Button) dialog.findViewById(R.id.dot);
        save = (Button) dialog.findViewById(R.id.save);

        focusedTextView = received;
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                focusedTextView.setText(focusedTextView.getText().toString() + "1");
            }
        });
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                focusedTextView.setText(focusedTextView.getText().toString() + "2");
            }
        });
        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                focusedTextView.setText(focusedTextView.getText().toString() + "3");
            }
        });
        b4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                focusedTextView.setText(focusedTextView.getText().toString() + "4");
            }
        });
        b5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                focusedTextView.setText(focusedTextView.getText().toString() + "5");
            }
        });
        b6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                focusedTextView.setText(focusedTextView.getText().toString() + "6");
            }
        });
        b7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                focusedTextView.setText(focusedTextView.getText().toString() + "7");
            }
        });
        b8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                focusedTextView.setText(focusedTextView.getText().toString() + "8");
            }
        });
        b9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                focusedTextView.setText(focusedTextView.getText().toString() + "9");
            }
        });
        b0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                focusedTextView.setText(focusedTextView.getText().toString() + "0");
            }
        });
        dot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (flag == 0) {
                    focusedTextView.setText(focusedTextView.getText().toString() + ".");
                    flag = 1;
                }
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String t0 = balance.getText().toString();
                String t1 = received.getText().toString();
                String t2 = cashMoney.getText().toString();

                Date currentTimeAndDate = Calendar.getInstance().getTime();
                SimpleDateFormat df1 = new SimpleDateFormat("dd-MM-yyyy");
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM");
                String today = convertToEnglish(df1.format(currentTimeAndDate));

                if (t1.equals(""))
                    Toast.makeText(PayMethods.this, getResources().getString(R.string.enter_recived_value), Toast.LENGTH_SHORT).show();
                else if ( // Double.parseDouble(t1) == Double.parseDouble(t2) &&
                        Double.parseDouble(t1) <= Double.parseDouble(t0)) {

                    cashValue += Double.parseDouble(t1);

                    if (!t2.equals("")) {
                        for (int i = 0; i < moneyList.size(); i++) {
                            TableRow tRow = (TableRow) tableLayout.getChildAt(i);
                            TextView x1 = (TextView) tRow.getChildAt(2);
                            TextView x2 = (TextView) tRow.getChildAt(1);
                            if (!x1.getText().toString().equals("0") && !x1.getText().toString().equals("")) {
                                Cashier cashier = new Cashier();
                                ArrayList<Cashier> cashiersList = new ArrayList<Cashier>();
                                cashier.setCashierName(Settings.user_name);
                                cashier.setCategoryName(x2.getText().toString());
                                cashier.setCategoryQty(Integer.parseInt(x1.getText().toString()));
                                cashier.setCategoryValue(Double.parseDouble(x2.getTag().toString()) * Integer.parseInt(x1.getText().toString()));
                                cashier.setCheckInDate(today);
                                cashier.setOrderKind(1);


                                cashiersList.add(cashier);

                                mDHandler.addCashierInOut(cashiersList);
                            }
                        }
                    }

                    dialog.dismiss();
                    Toast.makeText(PayMethods.this, getResources().getString(R.string.save), Toast.LENGTH_SHORT).show();
                    if (cashValue != 0) {
                        cash.setText(getResources().getString(R.string.cash) + " : " + cashValue);
                        cash.setBackgroundDrawable(getResources().getDrawable(R.drawable.clear_buttons));
                        mainBalance = "" + (Double.parseDouble(mainBalance) - Double.parseDouble(t1));
                        remainingBalance.setText(getResources().getString(R.string.remaining_) + mainBalance);
                    }
                } else if(Double.parseDouble(t1)> Double.parseDouble(t0)) {
                    Toast.makeText(PayMethods.this, getResources().getString(R.string.invaled_input), Toast.LENGTH_SHORT).show();
                    double received_value = Double.parseDouble(t1) - Double.parseDouble(t0);
                    payGraterDialog(String.valueOf(received_value), today, t0);
                    cashValue += Double.parseDouble(t0);
                    if (cashValue != 0) {
                        cash.setText(getResources().getString(R.string.cash) + " : " + cashValue);
                        cash.setBackgroundDrawable(getResources().getDrawable(R.drawable.clear_buttons));
                        mainBalance = "" + (Double.parseDouble(mainBalance) - Double.parseDouble(t0));
                        remainingBalance.setText(getResources().getString(R.string.remaining_) + mainBalance);
                    }
                    dialog.dismiss();
                }
            }
        });
        dialog.show();
    }


    void payGraterDialog(String message,String today,String balance_value) {
        Dialog dialog2 = new Dialog(PayMethods.this);
        dialog2.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog2.setContentView(R.layout.pay_grater_than);
        dialog2.setCanceledOnTouchOutside(false);
        dialog2.setCancelable(false);

        TextView mess=(TextView)dialog2.findViewById(R.id.text_return);
        Button b_mess=(Button) dialog2.findViewById(R.id.b_done);

        b_mess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cashier cashier = new Cashier();
                ArrayList<Cashier> cashiersList = new ArrayList<Cashier>();
                cashier.setCashierName(Settings.user_name);
                cashier.setCategoryName("null");
                cashier.setCategoryQty(-1);
                cashier.setCategoryValue(Double.parseDouble(balance_value));
                cashier.setCheckInDate(today);
                cashier.setOrderKind(1);

                cashiersList.add(cashier);
                mDHandler.addCashierInOut(cashiersList);
                Toast.makeText(PayMethods.this, getResources().getString(R.string.save), Toast.LENGTH_SHORT).show();
                dialog2.dismiss();
            }
        });

        mess.setText(twoDForm.format(Double.parseDouble(convertToEnglish(message))));

        dialog2.show();
    }

    @SuppressLint("ClickableViewAccessibility")
    void showCreditCardDialog() {

        dialog = new Dialog(PayMethods.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.credit_card_dialog);
        dialog.setCanceledOnTouchOutside(true);

//        Window window = dialog.getWindow();
//        window.setLayout(900, 415);

        final TextView balance = (TextView) dialog.findViewById(R.id.balance);
        final TextView received = (TextView) dialog.findViewById(R.id.received);
        final TextView cardNo = (TextView) dialog.findViewById(R.id.card_number);
        final Button addCredit = (Button) dialog.findViewById(R.id.add_credit);
        final Spinner spinner = (Spinner) dialog.findViewById(R.id.card_name_spinner);

        received.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                flag = 0;
                received.setText("");
                focusedTextView = received;
                return true;
            }
        });

        cardNo.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                cardNo.setText("");
                focusedTextView = cardNo;
                return true;
            }
        });

        flag = 0;
        balance.setText(mainBalance);
        Button b1, b2, b3, b4, b5, b6, b7, b8, b9, b0, dot, save;
        b1 = (Button) dialog.findViewById(R.id.b1);
        b2 = (Button) dialog.findViewById(R.id.b2);
        b3 = (Button) dialog.findViewById(R.id.b3);
        b4 = (Button) dialog.findViewById(R.id.b4);
        b5 = (Button) dialog.findViewById(R.id.b5);
        b6 = (Button) dialog.findViewById(R.id.b6);
        b7 = (Button) dialog.findViewById(R.id.b7);
        b8 = (Button) dialog.findViewById(R.id.b8);
        b9 = (Button) dialog.findViewById(R.id.b9);
        b0 = (Button) dialog.findViewById(R.id.b0);
        dot = (Button) dialog.findViewById(R.id.dot);
        save = (Button) dialog.findViewById(R.id.save);

        final ArrayList<CreditCard> creditCards = mDHandler.getAllCreditCards();
        creditCardsName = new ArrayList();
//        creditCardsName.add("");

        if (creditCards.size() > 0)
            for (int i = creditCards.size() - 1; i >= 0; i--) {
                creditCardsName.add(creditCards.get(i).getCardName());
            }

        adapter = new ArrayAdapter<String>(PayMethods.this, R.layout.spinner_style, creditCardsName);
        spinner.setAdapter(adapter);

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (focusedTextView != null)
                    focusedTextView.setText(focusedTextView.getText().toString() + "1");
            }
        });
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (focusedTextView != null)
                    focusedTextView.setText(focusedTextView.getText().toString() + "2");
            }
        });
        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (focusedTextView != null)
                    focusedTextView.setText(focusedTextView.getText().toString() + "3");
            }
        });
        b4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (focusedTextView != null)
                    focusedTextView.setText(focusedTextView.getText().toString() + "4");
            }
        });
        b5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (focusedTextView != null)
                    focusedTextView.setText(focusedTextView.getText().toString() + "5");
            }
        });
        b6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (focusedTextView != null)
                    focusedTextView.setText(focusedTextView.getText().toString() + "6");
            }
        });
        b7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (focusedTextView != null)
                    focusedTextView.setText(focusedTextView.getText().toString() + "7");
            }
        });
        b8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (focusedTextView != null)
                    focusedTextView.setText(focusedTextView.getText().toString() + "8");
            }
        });
        b9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (focusedTextView != null)
                    focusedTextView.setText(focusedTextView.getText().toString() + "9");
            }
        });
        b0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (focusedTextView != null)
                    focusedTextView.setText(focusedTextView.getText().toString() + "0");
            }
        });
        dot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (flag == 0) {
                    if (focusedTextView != null) {
                        focusedTextView.setText(focusedTextView.getText().toString() + ".");
                        flag = 1;
                    }
                }
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String t0 = balance.getText().toString();
                String t1 = received.getText().toString();
                String t2 = cardNo.getText().toString();
                //&& spinner.getSelectedItem().toString().equals("")
                if (!t1.equals("") && !t2.equals("") && creditCardsName.size() != 0) {

                    if (Double.parseDouble(t1) <= Double.parseDouble(t0)) {

                        creditCardValue += Double.parseDouble(t1);
                        dialog.dismiss();
                        Toast.makeText(PayMethods.this, getResources().getString(R.string.save), Toast.LENGTH_SHORT).show();
                        if (creditCardValue != 0) {
                            creditCard.setText(getResources().getString(R.string.credit_card) + " : " + creditCardValue);
                            creditCard.setBackgroundDrawable(getResources().getDrawable(R.drawable.clear_buttons));
                            mainBalance = "" + (Double.parseDouble(mainBalance) - Double.parseDouble(t1));
                            remainingBalance.setText(getResources().getString(R.string.remaining_) + mainBalance);

                            resiveCredit.add(countCridit, received.getText().toString());
                            cardNumbers.add(countCridit, cardNo.getText().toString());
                            cardName.add(countCridit, spinner.getSelectedItem().toString());
                        }
                    } else
                        Toast.makeText(PayMethods.this, getResources().getString(R.string.invaled_input), Toast.LENGTH_SHORT).show();

                } else
                    Toast.makeText(PayMethods.this, getResources().getString(R.string.enter_recived_value_and_carfno), Toast.LENGTH_LONG).show();

            }
        });
        addCredit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                showAddCreditCardDialog();
            }
        });


        dialog.show();
    }

    void showAddCreditCardDialog() {
        dialog1 = new Dialog(PayMethods.this);
        dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog1.setCancelable(true);
        dialog1.setContentView(R.layout.add_credit_card_dialog);
        dialog1.setCanceledOnTouchOutside(true);

//        Window window = dialog1.getWindow();
//        window.setLayout(600, 310);

        Button Addindb = (Button) dialog1.findViewById(R.id.addcard);
        TextView serial_txt = (TextView) dialog1.findViewById(R.id.serial_t);
        final EditText Card_NA = (EditText) dialog1.findViewById(R.id.CARDnAME);
        final EditText ACC_card = (EditText) dialog1.findViewById(R.id.Acccode);

        final ArrayList<CreditCard> card = mDHandler.getAllCreditCards();
        serial_txt.setText("" + (card.size() + 1));

        final CreditCard card_iteam = new CreditCard();

        Addindb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!Card_NA.getText().toString().equals("") && !ACC_card.getText().toString().equals("")) {
                    card_iteam.setSerial(card.size() + 1);
                    card_iteam.setCardName(Card_NA.getText().toString());
                    card_iteam.setAccCode(ACC_card.getText().toString());

                    mDHandler.addCreditCard(card_iteam);

                    creditCardsName.add(0, card_iteam.getCardName());
                    adapter.notifyDataSetChanged();
                    dialog1.dismiss();
                } else
                    Toast.makeText(PayMethods.this, getResources().getString(R.string.fill_request_filed), Toast.LENGTH_SHORT).show();
            }
        });
        dialog1.show();
    }

    @SuppressLint("ClickableViewAccessibility")
    void showChequeDialog() {
        dialog = new Dialog(PayMethods.this); //
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.cheque_dialog);
        dialog.setCanceledOnTouchOutside(true);

//        Window window = dialog.getWindow();
//        window.setLayout(940, 415);

        final TextView balance = (TextView) dialog.findViewById(R.id.balance);
        final TextView received = (TextView) dialog.findViewById(R.id.received);
        final TextView chequeNumber = (TextView) dialog.findViewById(R.id.cheque_number);
        final Button addBank = (Button) dialog.findViewById(R.id.add_bank);
        final Spinner spinner2 = (Spinner) dialog.findViewById(R.id.bank_name_spinner);

        balance.setText(mainBalance);
        flag = 0;
        Button b1, b2, b3, b4, b5, b6, b7, b8, b9, b0, dot, save;
        b1 = (Button) dialog.findViewById(R.id.b1);
        b2 = (Button) dialog.findViewById(R.id.b2);
        b3 = (Button) dialog.findViewById(R.id.b3);
        b4 = (Button) dialog.findViewById(R.id.b4);
        b5 = (Button) dialog.findViewById(R.id.b5);
        b6 = (Button) dialog.findViewById(R.id.b6);
        b7 = (Button) dialog.findViewById(R.id.b7);
        b8 = (Button) dialog.findViewById(R.id.b8);
        b9 = (Button) dialog.findViewById(R.id.b9);
        b0 = (Button) dialog.findViewById(R.id.b0);
        dot = (Button) dialog.findViewById(R.id.dot);
        save = (Button) dialog.findViewById(R.id.save);

        received.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                flag = 0;
                received.setText("");
                focusedTextView = received;
                return true;
            }
        });

        chequeNumber.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                chequeNumber.setText("");
                focusedTextView = chequeNumber;
                return true;
            }
        });


        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (focusedTextView != null)
                    focusedTextView.setText(focusedTextView.getText().toString() + "1");
            }
        });
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (focusedTextView != null)
                    focusedTextView.setText(focusedTextView.getText().toString() + "2");
            }
        });
        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (focusedTextView != null)
                    focusedTextView.setText(focusedTextView.getText().toString() + "3");
            }
        });
        b4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (focusedTextView != null)
                    focusedTextView.setText(focusedTextView.getText().toString() + "4");
            }
        });
        b5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (focusedTextView != null)
                    focusedTextView.setText(focusedTextView.getText().toString() + "5");
            }
        });
        b6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (focusedTextView != null)
                    focusedTextView.setText(focusedTextView.getText().toString() + "6");
            }
        });
        b7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (focusedTextView != null)
                    focusedTextView.setText(focusedTextView.getText().toString() + "7");
            }
        });
        b8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (focusedTextView != null)
                    focusedTextView.setText(focusedTextView.getText().toString() + "8");
            }
        });
        b9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (focusedTextView != null)
                    focusedTextView.setText(focusedTextView.getText().toString() + "9");
            }
        });
        b0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (focusedTextView != null)
                    focusedTextView.setText(focusedTextView.getText().toString() + "0");
            }
        });
        dot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (flag == 0) {
                    if (focusedTextView != null) {
                        focusedTextView.setText(focusedTextView.getText().toString() + ".");
                        flag = 1;
                    }
                }
            }
        });

        chequeListName = new ArrayList();

        final ArrayList<Cheque> chequeList = mDHandler.getAllCheques();
        final int serial = chequeList.size() + 1;
        Log.e("*serial*", "***" + chequeList.size() + "***  " + serial);

        chequeListName.add("");
        if (chequeList.size() > 0)
            for (int i = chequeList.size() - 1; i >= 0; i--) {
                chequeListName.add(chequeList.get(i).getBankName());
            }

        adapter2 = new ArrayAdapter<String>(PayMethods.this, R.layout.spinner_style, chequeListName);
        spinner2.setAdapter(adapter2);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                String t0 = balance.getText().toString();
                String t1 = received.getText().toString();
                String t2 = chequeNumber.getText().toString();

                if (t1.equals("") && t2.equals("") && spinner2.getSelectedItem().toString().equals(""))
                    Toast.makeText(PayMethods.this, getResources().getString(R.string.enter_recived_value_and_carfno), Toast.LENGTH_LONG).show();
                else if (Double.parseDouble(t1) <= Double.parseDouble(t0)) {

                    chequeValue += Double.parseDouble(t1);
                    dialog.dismiss();
                    Toast.makeText(PayMethods.this, getResources().getString(R.string.save), Toast.LENGTH_SHORT).show();
                    if (chequeValue != 0) {
                        cheque.setText(getResources().getString(R.string.cheque) + " : " + chequeValue);
                        cheque.setBackgroundDrawable(getResources().getDrawable(R.drawable.clear_buttons));
                        mainBalance = "" + (Double.parseDouble(mainBalance) - Double.parseDouble(t1));
                        remainingBalance.setText(getResources().getString(R.string.remaining_) + mainBalance);

                        resiveCheque.add(received.getText().toString());
                        chequeNambers.add(chequeNumber.getText().toString());
                        bankName.add(countCheque, spinner2.getSelectedItem().toString());

                        Cheque obj = new Cheque();
                        obj.setSerialCheque(serial);
                        obj.setBankName(spinner2.getSelectedItem().toString());
                        obj.setChequeNumber(Integer.parseInt(chequeNumber.getText().toString()));
                        obj.setReceived(Double.parseDouble(received.getText().toString())); ///very important must change to double
                        mDHandler.addCheque(obj);
                    }
                } else
                    Toast.makeText(PayMethods.this, getResources().getString(R.string.invaled_input), Toast.LENGTH_SHORT).show();
            }
        });
        addBank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                showAddBankNameDialog();
            }
        });


        dialog.show();
    }

    void showAddBankNameDialog() {
        dialog1 = new Dialog(PayMethods.this);
        dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog1.setCancelable(true);
        dialog1.setContentView(R.layout.add_bank_dialog);
        dialog1.setCanceledOnTouchOutside(true);

//        Window window = dialog1.getWindow();
//        window.setLayout(500, 270);

        Button add = (Button) dialog1.findViewById(R.id.ADDBank);
        final EditText bankNameEditText = (EditText) dialog1.findViewById(R.id.bank);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!bankNameEditText.getText().toString().equals("")) {

                    chequeListName.add(0, bankNameEditText.getText().toString());

                    adapter2.notifyDataSetChanged();
                    dialog1.dismiss();
                } else
                    Toast.makeText(PayMethods.this, getResources().getString(R.string.fill_request_filed), Toast.LENGTH_SHORT).show();
            }
        });
        dialog1.show();

    }

    @SuppressLint("ClickableViewAccessibility")
    void showGiftCardDialog() {
        dialog = new Dialog(PayMethods.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.gift_card_dialog);
        dialog.setCanceledOnTouchOutside(true);

//        Window window = dialog.getWindow();
//        window.setLayout(970, 460);

        flag = 0;
        final Button confirm = (Button) dialog.findViewById(R.id.confirm);
        final TextView balance = (TextView) dialog.findViewById(R.id.balance);
        final TextView received = (TextView) dialog.findViewById(R.id.received);
        final TextView cardNo = (TextView) dialog.findViewById(R.id.card_number);
        final TableLayout cardInfo = (TableLayout) dialog.findViewById(R.id.card_info);

        balance.setText(mainBalance);
        received.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                flag = 0;
                received.setText("");
                focusedTextView = received;
                return true;
            }
        });

        cardNo.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                cardNo.setText("");
                focusedTextView = cardNo;
                return true;
            }
        });
        Button b1, b2, b3, b4, b5, b6, b7, b8, b9, b0, dot, save;
        b1 = (Button) dialog.findViewById(R.id.b1);
        b2 = (Button) dialog.findViewById(R.id.b2);
        b3 = (Button) dialog.findViewById(R.id.b3);
        b4 = (Button) dialog.findViewById(R.id.b4);
        b5 = (Button) dialog.findViewById(R.id.b5);
        b6 = (Button) dialog.findViewById(R.id.b6);
        b7 = (Button) dialog.findViewById(R.id.b7);
        b8 = (Button) dialog.findViewById(R.id.b8);
        b9 = (Button) dialog.findViewById(R.id.b9);
        b0 = (Button) dialog.findViewById(R.id.b0);
        dot = (Button) dialog.findViewById(R.id.dot);
        save = (Button) dialog.findViewById(R.id.save);

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (focusedTextView != null)
                    focusedTextView.setText(focusedTextView.getText().toString() + "1");
            }
        });
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (focusedTextView != null)
                    focusedTextView.setText(focusedTextView.getText().toString() + "2");
            }
        });
        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (focusedTextView != null)
                    focusedTextView.setText(focusedTextView.getText().toString() + "3");
            }
        });
        b4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (focusedTextView != null)
                    focusedTextView.setText(focusedTextView.getText().toString() + "4");
            }
        });
        b5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (focusedTextView != null)
                    focusedTextView.setText(focusedTextView.getText().toString() + "5");
            }
        });
        b6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (focusedTextView != null)
                    focusedTextView.setText(focusedTextView.getText().toString() + "6");
            }
        });
        b7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (focusedTextView != null)
                    focusedTextView.setText(focusedTextView.getText().toString() + "7");
            }
        });
        b8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (focusedTextView != null)
                    focusedTextView.setText(focusedTextView.getText().toString() + "8");
            }
        });
        b9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (focusedTextView != null)
                    focusedTextView.setText(focusedTextView.getText().toString() + "9");
            }
        });
        b0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (focusedTextView != null)
                    focusedTextView.setText(focusedTextView.getText().toString() + "0");
            }
        });
        dot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (flag == 0) {
                    if (focusedTextView != null) {
                        focusedTextView.setText(focusedTextView.getText().toString() + ".");
                        flag = 1;
                    }
                }
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String t0 = balance.getText().toString();
                String t1 = received.getText().toString();
                String t2 = cardNo.getText().toString();

                if (t1.equals("") && t2.equals(""))
                    Toast.makeText(PayMethods.this, getResources().getString(R.string.enter_recived_value_and_carfno), Toast.LENGTH_LONG).show();
                else if (Double.parseDouble(t1) <= Double.parseDouble(t0)) {

                    giftCardValue += Double.parseDouble(t1);
                    dialog.dismiss();
                    Toast.makeText(PayMethods.this, getResources().getString(R.string.save), Toast.LENGTH_SHORT).show();
                    if (giftCardValue != 0) {
                        giftCard.setText(getResources().getString(R.string.gift_card) + " : " + giftCardValue);
                        giftCard.setBackgroundDrawable(getResources().getDrawable(R.drawable.clear_buttons));
                        mainBalance = "" + (Double.parseDouble(mainBalance) - Double.parseDouble(t1));
                        remainingBalance.setText(getResources().getString(R.string.remaining_) + mainBalance);

                        resiveGift.add(received.getText().toString());
                        giftCardNumber.add(cardNo.getText().toString());
                    }
                } else
                    Toast.makeText(PayMethods.this, getResources().getString(R.string.invaled_input), Toast.LENGTH_SHORT).show();

            }
        });
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!cardNo.getText().toString().equals("")) {
                    double cardNumber = Double.parseDouble(cardNo.getText().toString());
                    if (true) {
                        cardInfo.setVisibility(View.VISIBLE);
                    } else {
                        Toast.makeText(PayMethods.this, getResources().getString(R.string.invalid_card_no), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });


        dialog.show();

    }

    @SuppressLint("ClickableViewAccessibility")
    void showCardDialog() {
        dialog = new Dialog(PayMethods.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.card_dialog);
        dialog.setCanceledOnTouchOutside(true);

//        Window window = dialog.getWindow();
//        window.setLayout(970, 420);

        flag = 0;
        final Button accept = (Button) dialog.findViewById(R.id.accept);
        final TextView balance = (TextView) dialog.findViewById(R.id.balance);
        final TextView couponNo = (TextView) dialog.findViewById(R.id.coupon_number);
        final TableLayout couponInfo = (TableLayout) dialog.findViewById(R.id.coupon_info);

        balance.setText(mainBalance);
        Button b1, b2, b3, b4, b5, b6, b7, b8, b9, b0, dot, save;
        b1 = (Button) dialog.findViewById(R.id.b1);
        b2 = (Button) dialog.findViewById(R.id.b2);
        b3 = (Button) dialog.findViewById(R.id.b3);
        b4 = (Button) dialog.findViewById(R.id.b4);
        b5 = (Button) dialog.findViewById(R.id.b5);
        b6 = (Button) dialog.findViewById(R.id.b6);
        b7 = (Button) dialog.findViewById(R.id.b7);
        b8 = (Button) dialog.findViewById(R.id.b8);
        b9 = (Button) dialog.findViewById(R.id.b9);
        b0 = (Button) dialog.findViewById(R.id.b0);
        dot = (Button) dialog.findViewById(R.id.dot);
        save = (Button) dialog.findViewById(R.id.save);

        couponNo.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                flag = 0;
                couponNo.setText("");
                return true;
            }
        });
        focusedTextView = couponNo;
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (focusedTextView != null)
                    focusedTextView.setText(focusedTextView.getText().toString() + "1");
            }
        });
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (focusedTextView != null)
                    focusedTextView.setText(focusedTextView.getText().toString() + "2");
            }
        });
        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (focusedTextView != null)
                    focusedTextView.setText(focusedTextView.getText().toString() + "3");
            }
        });
        b4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (focusedTextView != null)
                    focusedTextView.setText(focusedTextView.getText().toString() + "4");
            }
        });
        b5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (focusedTextView != null)
                    focusedTextView.setText(focusedTextView.getText().toString() + "5");
            }
        });
        b6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (focusedTextView != null)
                    focusedTextView.setText(focusedTextView.getText().toString() + "6");
            }
        });
        b7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (focusedTextView != null)
                    focusedTextView.setText(focusedTextView.getText().toString() + "7");
            }
        });
        b8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (focusedTextView != null)
                    focusedTextView.setText(focusedTextView.getText().toString() + "8");
            }
        });
        b9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (focusedTextView != null)
                    focusedTextView.setText(focusedTextView.getText().toString() + "9");
            }
        });
        b0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (focusedTextView != null)
                    focusedTextView.setText(focusedTextView.getText().toString() + "0");
            }
        });
        dot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (flag == 0) {
                    if (focusedTextView != null) {
                        focusedTextView.setText(focusedTextView.getText().toString() + ".");
                        flag = 1;
                    }
                }
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String t0 = balance.getText().toString();
                String t1 = couponNo.getText().toString();

                if (t1.equals(""))
                    Toast.makeText(PayMethods.this, getResources().getString(R.string.enter_coupon_no), Toast.LENGTH_LONG).show();
                else if (Double.parseDouble(t1) <= Double.parseDouble(t0)) {

                    creditValue += Double.parseDouble(t1);
                    dialog.dismiss();
                    Toast.makeText(PayMethods.this, getResources().getString(R.string.save), Toast.LENGTH_SHORT).show();
                    if (creditValue != 0) {
                        credit.setText(getResources().getString(R.string.credit) + " : " + creditValue);
                        credit.setBackgroundDrawable(getResources().getDrawable(R.drawable.clear_buttons));
                        mainBalance = "" + (Double.parseDouble(mainBalance) - Double.parseDouble(t1));
                        remainingBalance.setText(getResources().getString(R.string.remaining_) + mainBalance);
                        couponNumber.add(countCoupon, couponNo.getText().toString());
                    }
                } else
                    Toast.makeText(PayMethods.this, getResources().getString(R.string.invaled_input), Toast.LENGTH_SHORT).show();

            }
        });
        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!couponNo.getText().toString().equals("")) {
                    double couponNumber = Double.parseDouble(couponNo.getText().toString());
                    if (true) {
                        couponInfo.setVisibility(View.VISIBLE);
                    } else {
                        Toast.makeText(PayMethods.this, getResources().getString(R.string.invalid_coupon_no), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });


        dialog.show();

    }

    @SuppressLint("ClickableViewAccessibility")
    void showPointDialog() {
        dialog = new Dialog(PayMethods.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.point_dialog);
        dialog.setCanceledOnTouchOutside(true);

//        Window window = dialog.getWindow();
//        window.setLayout(970, 480);

        flag = 0;
        final Button confirm = (Button) dialog.findViewById(R.id.confirm);
        final TextView balance = (TextView) dialog.findViewById(R.id.balance);
        final TextView received = (TextView) dialog.findViewById(R.id.received);
        final TextView cardNo = (TextView) dialog.findViewById(R.id.card_number);
        final TableLayout cardInfo = (TableLayout) dialog.findViewById(R.id.card_info);

        balance.setText(mainBalance);
        received.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                flag = 0;
                received.setText("");
                focusedTextView = received;
                return true;
            }
        });

        cardNo.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                cardNo.setText("");
                focusedTextView = cardNo;
                return true;
            }
        });

        Button b1, b2, b3, b4, b5, b6, b7, b8, b9, b0, dot, save;
        b1 = (Button) dialog.findViewById(R.id.b1);
        b2 = (Button) dialog.findViewById(R.id.b2);
        b3 = (Button) dialog.findViewById(R.id.b3);
        b4 = (Button) dialog.findViewById(R.id.b4);
        b5 = (Button) dialog.findViewById(R.id.b5);
        b6 = (Button) dialog.findViewById(R.id.b6);
        b7 = (Button) dialog.findViewById(R.id.b7);
        b8 = (Button) dialog.findViewById(R.id.b8);
        b9 = (Button) dialog.findViewById(R.id.b9);
        b0 = (Button) dialog.findViewById(R.id.b0);
        dot = (Button) dialog.findViewById(R.id.dot);
        save = (Button) dialog.findViewById(R.id.save);

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (focusedTextView != null)
                    focusedTextView.setText(focusedTextView.getText().toString() + "1");
            }
        });
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (focusedTextView != null)
                    focusedTextView.setText(focusedTextView.getText().toString() + "2");
            }
        });
        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (focusedTextView != null)
                    focusedTextView.setText(focusedTextView.getText().toString() + "3");
            }
        });
        b4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (focusedTextView != null)
                    focusedTextView.setText(focusedTextView.getText().toString() + "4");
            }
        });
        b5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (focusedTextView != null)
                    focusedTextView.setText(focusedTextView.getText().toString() + "5");
            }
        });
        b6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (focusedTextView != null)
                    focusedTextView.setText(focusedTextView.getText().toString() + "6");
            }
        });
        b7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (focusedTextView != null)
                    focusedTextView.setText(focusedTextView.getText().toString() + "7");
            }
        });
        b8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (focusedTextView != null)
                    focusedTextView.setText(focusedTextView.getText().toString() + "8");
            }
        });
        b9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (focusedTextView != null)
                    focusedTextView.setText(focusedTextView.getText().toString() + "9");
            }
        });
        b0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (focusedTextView != null)
                    focusedTextView.setText(focusedTextView.getText().toString() + "0");
            }
        });
        dot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (flag == 0) {
                    if (focusedTextView != null) {
                        focusedTextView.setText(focusedTextView.getText().toString() + ".");
                        flag = 1;
                    }
                }
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String t0 = balance.getText().toString();
                String t1 = received.getText().toString();
                String t2 = cardNo.getText().toString();

                if (t1.equals("") && t2.equals(""))
                    Toast.makeText(PayMethods.this, getResources().getString(R.string.enter_recived_value_and_carfno), Toast.LENGTH_LONG).show();
                else if (Double.parseDouble(t1) <= Double.parseDouble(t0)) {

                    pointValue += Double.parseDouble(t1);
                    dialog.dismiss();
                    Toast.makeText(PayMethods.this, getResources().getString(R.string.save), Toast.LENGTH_SHORT).show();
                    if (pointValue != 0) {
                        point.setText(getResources().getString(R.string.point) + " : " + pointValue);
                        point.setBackgroundDrawable(getResources().getDrawable(R.drawable.clear_buttons));
                        mainBalance = "" + (Double.parseDouble(mainBalance) - Double.parseDouble(t1));
                        remainingBalance.setText(getResources().getString(R.string.remaining_) + mainBalance);

                        resivePoint.add(received.getText().toString());
                        pointCardNumber.add(cardNo.getText().toString());
                    }
                } else
                    Toast.makeText(PayMethods.this, getResources().getString(R.string.invaled_input), Toast.LENGTH_SHORT).show();

            }
        });
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!cardNo.getText().toString().equals("")) {
                    double cardNumber = Double.parseDouble(cardNo.getText().toString());
                    if (true) {
                        cardInfo.setVisibility(View.VISIBLE);
                    } else {
                        Toast.makeText(PayMethods.this, getResources().getString(R.string.invalid_card_no), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });


        dialog.show();

    }

    public void saveInDataBase() {

        String balanceTest = remainingBalance.getText().toString().substring(remainingBalance.getText().toString().indexOf(":") + 1);
        Log.e("test ...", "balanceTest--->   " + Double.parseDouble(balanceTest));
        if (Double.parseDouble(balanceTest) == 0.00) {
            Date currentTimeAndDate = Calendar.getInstance().getTime();
            SimpleDateFormat df1 = new SimpleDateFormat("dd-MM-yyyy");
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM");
            SimpleDateFormat Tf = new SimpleDateFormat("HH:mm:ss");
            String today = convertToEnglish(df1.format(currentTimeAndDate));
            String times = convertToEnglish(Tf.format(currentTimeAndDate));
            int serial; //= mDHandler.getMaxSerial("VOUCHER_SERIAL", "PAY_METHOD");
            if (orderHeaderTemp == null) {
                serial = obj.getOrderHeaderObj().getVoucherSerial();
            } else {
                serial = orderHeaderTemp.get(0).getVoucherSerial();
            }
            String vhfSerial = convertToEnglish(df.format(currentTimeAndDate));// + "-" + (serial);
            String newString = convertToEnglish(vhfSerial.replace("-", "") + "-" + (serial));

            double cashValues = 0, cardValues = 0, chequeValues = 0, giftValues = 0, couponValues = 0, pointValues = 0;
            List<PayMethod> payMethodList = new ArrayList<>();
            PayMethod payMethod = new PayMethod();

            payMethod.setOrderType(orderType.equals("Take Away") ? 0 : 1);
            payMethod.setOrderKind(0);
            payMethod.setVoucherDate(today);
            payMethod.setPointOfSaleNumber(Settings.POS_number);
            payMethod.setStoreNumber(Settings.store_number);
            payMethod.setVoucherNumber("" + serial);
            payMethod.setVoucherSerial(serial);
            payMethod.setShiftName(Settings.shift_name);
            payMethod.setShiftNumber(Settings.shift_number);
            payMethod.setUserNo(Settings.user_no);
            payMethod.setUserName(Settings.user_name);
            payMethod.setTime(times);
            payMethod.setOrgPos(-1);
            payMethod.setOrgNo("0");

            if (cashValue != 0.00) {
                payMethod.setPayType("Cash");
                payMethod.setPayValue(cashValue);
                payMethod.setPayNumber("0");
                payMethod.setPayName("null");
                mDHandler.addAllPayMethodItem(payMethod);
                payMethodList.add(payMethod);
                cashValue1 = cashValue;
                cashValue = 0.00;
            }
            if (creditCardValue != 0.00) {
                for (int i = 0; i < cardNumbers.size(); i++) {
                    payMethod.setPayType("Credit Card");
                    payMethod.setPayValue(Double.parseDouble(resiveCredit.get(i)));
                    payMethod.setPayNumber(cardNumbers.get(i));
                    payMethod.setPayName(cardName.get(i));
                    mDHandler.addAllPayMethodItem(payMethod);
                    payMethodList.add(payMethod);
                    cardValues += Double.parseDouble(resiveCredit.get(i));
                    Log.e("card value", String.valueOf(cardValues));
                }
                cardNumbers.clear();
                countCridit = 0;
                cardName.clear();
                resiveCredit.clear();
                creditCardValue1 = cardValues;
                creditCardValue = 0.00;
            }
            if (chequeValue != 0.00) {
                for (int i = 0; i < chequeNambers.size(); i++) {
                    payMethod.setPayType("Cheque");
                    payMethod.setPayValue(Double.parseDouble(resiveCheque.get(i)));
                    payMethod.setPayNumber(chequeNambers.get(i));
                    payMethod.setPayName(bankName.get(i));
                    mDHandler.addAllPayMethodItem(payMethod);
                    payMethodList.add(payMethod);
                    chequeValues += Double.parseDouble(resiveCheque.get(i));
                    Log.e("chequeValues", String.valueOf(chequeValues));
                }
                chequeValue1 = chequeValues;
                chequeValue = 0.00;
                chequeNambers.clear();
                bankName.clear();
                resiveCheque.clear();
                countCheque = 0;
            }
            if (giftCardValue != 0.00) {
                for (int i = 0; i < giftCardNumber.size(); i++) {
                    payMethod.setPayType("Gift Card");
                    payMethod.setPayValue(Double.parseDouble(resiveGift.get(i)));
                    payMethod.setPayNumber(giftCardNumber.get(i));
                    payMethod.setPayName("null");
                    mDHandler.addAllPayMethodItem(payMethod);
                    payMethodList.add(payMethod);
                    giftValues += Double.parseDouble(resiveGift.get(i));
                    Log.e("giftValues", String.valueOf(giftValues));
                }
                giftCardValue1 = giftValues;
                giftCardValue = 0.00;
                giftCardNumber.clear();
                resiveGift.clear();
                countGift = 0;
            }
            Log.e("creditValue", "" + creditValue);
            if (creditValue != 0.00) {
                for (int i = 0; i < couponNumber.size(); i++) {
                    payMethod.setPayType("Coupon");
                    payMethod.setPayValue(Double.parseDouble(couponNumber.get(i)));
                    payMethod.setPayNumber(couponNumber.get(i));
                    payMethod.setPayName("null");
                    mDHandler.addAllPayMethodItem(payMethod);
                    payMethodList.add(payMethod);
                    couponValues += Double.parseDouble(couponNumber.get(i));
                    Log.e("couponValues", String.valueOf(couponValues));
                }
                creditValue1 = couponValues;
                creditValue = 0.00;
                couponNumber.clear();
                countCoupon = 0;
            }
            if (pointValue != 0.00) {
                for (int i = 0; i < pointCardNumber.size(); i++) {
                    payMethod.setPayType("Point Card");
                    payMethod.setPayValue(Double.parseDouble(resivePoint.get(i)));
                    payMethod.setPayNumber(pointCardNumber.get(i));
                    payMethod.setPayName("null");
                    mDHandler.addAllPayMethodItem(payMethod);
                    payMethodList.add(payMethod);
                    pointValues += Double.parseDouble(resivePoint.get(i));
                    Log.e("pointValues", String.valueOf(pointValues));
                }
                pointValue1 = pointValues;
                pointValue = 0.00;
                countPoint = 0;
                pointCardNumber.clear();
                resivePoint.clear();
            }
            List<ItemWithScreen> itemWithScreens = mDHandler.getAllItemsWithScreen();

            if (orderHeaderTemp == null) { // Takeaway
                //getting the data from order activity and save it in database.
                Log.e("creditCardValue1", " " + creditCardValue1);
                obj.getOrderHeaderObj().setCashValue(cashValue1);
                obj.getOrderHeaderObj().setCardsValue(creditCardValue1);
                obj.getOrderHeaderObj().setChequeValue(chequeValue1);
                obj.getOrderHeaderObj().setGiftValue(giftCardValue1);
                obj.getOrderHeaderObj().setCouponValue(creditValue1);
                obj.getOrderHeaderObj().setPointValue(pointValue1);

                mDHandler.addOrderHeader(obj.getOrderHeaderObj());

                for (int i = 0; i < obj.getOrderTransactionObj().size(); i++)
                    mDHandler.addOrderTransaction(obj.getOrderTransactionObj().get(i));


                sendToKitchen(PayMethods.this,obj.getOrderHeaderObj(), obj.getOrderTransactionObj(), payMethodList,itemWithScreens);
                sendToServer(obj.getOrderHeaderObj(), obj.getOrderTransactionObj(), payMethodList);

//                Intent intent = new Intent(PayMethods.this, Order.class);
//                startActivity(intent);
                Print(obj.getOrderTransactionObj(),obj.getOrderHeaderObj());

            } else { // Dine In

                orderHeaderTemp.get(0).setCashValue(cashValue1);
                orderHeaderTemp.get(0).setCardsValue(creditCardValue1);
                orderHeaderTemp.get(0).setChequeValue(chequeValue1);
                orderHeaderTemp.get(0).setGiftValue(giftCardValue1);
                orderHeaderTemp.get(0).setCouponValue(creditValue1);
                orderHeaderTemp.get(0).setPointValue(pointValue1);

                mDHandler.addOrderHeader(orderHeaderTemp.get(0));
                for (int i = 0; i < orderTransTemp.size(); i++) {
                    mDHandler.addOrderTransaction(orderTransTemp.get(i));
                }
                mDHandler.deleteFromOrderHeaderTemp(sectionNo, tableNo);
                mDHandler.deleteFromOrderTransactionTemp(sectionNo, tableNo);

                sendToKitchen(PayMethods.this,orderHeaderTemp.get(0), orderTransTemp, payMethodList,itemWithScreens);
                sendToServer(orderHeaderTemp.get(0), orderTransTemp, payMethodList);

                Intent intent = new Intent(PayMethods.this, DineIn.class);
                startActivity(intent);
            }

            Toast.makeText(this, getResources().getString(R.string.save_successful), Toast.LENGTH_SHORT).show();
          //  finish();
//            Print(obj.getOrderTransactionObj());
        } else {
            Toast.makeText(this, getResources().getString(R.string.remaining_not_o), Toast.LENGTH_SHORT).show();
        }
    }

   public void sendToKitchen(Context context,OrderHeader OrderHeaderObj, List<OrderTransactions> OrderTransactionsObj, List<PayMethod> PayMethodObj, List<ItemWithScreen> itemWithScreens) {
        try {
            JSONObject obj1 = OrderHeaderObj.getJSONObject();

//            List<ItemWithScreen> itemWithScreens = mDHandler.getAllItemsWithScreen();
            for (int i = 0; i < OrderTransactionsObj.size(); i++) {
                for (int j = 0; j < itemWithScreens.size(); j++) {
                    if (OrderTransactionsObj.get(i).getItemBarcode().equals("" + itemWithScreens.get(j).getItemCode()))
                        OrderTransactionsObj.get(i).setScreenNo(itemWithScreens.get(j).getScreenNo());
                }
                OrderTransactionsObj.get(i).setNote("");
            }

//            for (int i = 0; i < OrderTransactionsObj.size(); i++) {
//                if (OrderTransactionsObj.get(i).getQty() == 0) {
//                    OrderTransactionsObj.get(i - 1).setNote((OrderTransactionsObj.get(i - 1).getNote()) + "\n" + OrderTransactionsObj.get(i).getItemName());
//                    OrderTransactionsObj.remove(i);
//                    i--;
//                }
//            }

            JSONArray obj2 = new JSONArray();
            for (int i = 0; i < OrderTransactionsObj.size(); i++)
                obj2.put(i, OrderTransactionsObj.get(i).getJSONObject());

            JSONObject obj = new JSONObject();
            obj.put("Items", obj2);
            obj.put("Header", obj1);

            Log.e("socket", "J");
            SendSocket sendSocket = new SendSocket(context, obj1, OrderTransactionsObj);
            sendSocket.sendMessage();

            Log.e("sendCloud", "J");
            SendCloud sendCloud = new SendCloud(context, obj);
            sendCloud.startSending("kitchen");

        } catch (JSONException e) {
            Log.e("Tag", "JSONException");
        }
    }

    void sendToServer(OrderHeader OrderHeaderObj, List<OrderTransactions> OrderTransactionsObj, List<PayMethod> PayMethodObj) {
        try {
            JSONObject obj1 = OrderHeaderObj.getJSONObject2();

            JSONArray obj2 = new JSONArray();
            for (int i = 0; i < OrderTransactionsObj.size(); i++)
                obj2.put(i, OrderTransactionsObj.get(i).getJSONObject2());

            JSONArray obj3 = new JSONArray();
            for (int i = 0; i < PayMethodObj.size(); i++)
                obj3.put(i, PayMethodObj.get(i).getJSONObject2());

            JSONObject obj = new JSONObject();
            obj.put("ORDERHEADER", obj1);
            obj.put("ORDERTRANSACTIONS", obj2);
            obj.put("PAYMETHOD", obj3);

            SendCloud sendCloud = new SendCloud(PayMethods.this, obj);
            sendCloud.startSending("Server");


        } catch (JSONException e) {
            Log.e("Tag", "JSONException");
        }
    }

    public String convertToEnglish(String value) {
        String newValue = (((((((((((value + "").replaceAll("١", "1")).replaceAll("٢", "2")).replaceAll("٣", "3")).replaceAll("٤", "4")).replaceAll("٥", "5")).replaceAll("٦", "6")).replaceAll("٧", "7")).replaceAll("٨", "8")).replaceAll("٩", "9")).replaceAll("٠", "0"));
        return newValue;
    }


    public void Print(List<OrderTransactions> OrderTransactionsObj,OrderHeader OrderHeaderObj) {
        Log.e("OrdedTr ", "" + OrderTransactionsObj.get(0).getTaxValue() + " date\n " + OrderTransactionsObj.get(0).getVoucherDate() + " \t no  " + OrderTransactionsObj.get(0).getVoucherNo());
        final Dialog dialog = new Dialog(PayMethods.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.printer);
        final Button okButton = dialog.findViewById(R.id.print_btn);
        final LinearLayout linearLayout = dialog.findViewById(R.id.linear2);
        TableLayout tabLayout = (TableLayout) dialog.findViewById(R.id.table_);
        TextView tax_no = (TextView) findViewById(R.id.textViewtaxnoText);
        TextView date = dialog.findViewById(R.id.textViewdateText);
        TextView time = dialog.findViewById(R.id.textViewtimeText);
        TextView w_date = dialog.findViewById(R.id.textViewWDAteText);
        TextView invoice_no = dialog.findViewById(R.id.textViewWInvoiceNoText);
        TextView casher = dialog.findViewById(R.id.textViewWCacherText);
        TextView total_money=dialog.findViewById(R.id.TotalValueMoneyText);
        if ((OrderTransactionsObj.get(0).getTaxValue())!=0.0)
        {
            tax_no.setText((OrderTransactionsObj.get(0).getTaxValue()) + "");
            Log.e("",""+OrderTransactionsObj.get(0).getTaxValue());
        }
       date.setText(OrderTransactionsObj.get(0).getVoucherDate());
       time.setText(OrderTransactionsObj.get(0).getTime());
       w_date.setText(OrderTransactionsObj.get(0).getVoucherDate());
       invoice_no.setText(OrderTransactionsObj.get(0).getVoucherNo());
       casher.setText(OrderTransactionsObj.get(0).getUserName());
       TableRow.LayoutParams lp2 = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 1.0f);

       lp2.setMargins(0, 7, 0, 0);

       final TableRow headerRow = new TableRow(PayMethods.this);
       headerRow.setBackgroundColor(getResources().getColor(R.color.light_blue));

       TextView header = new TextView(PayMethods.this);
       header.setGravity(Gravity.CENTER);
       header.setText("Item name ");
       header.setTextColor(getResources().getColor(R.color.text_color));
       header.setLayoutParams(lp2);
       header.setTextSize(14);
       headerRow.addView(header);

       TextView header2 = new TextView(PayMethods.this);
       header2.setGravity(Gravity.CENTER);
       header2.setText("QTy");
       header2.setTextColor(getResources().getColor(R.color.text_color));
       header2.setLayoutParams(lp2);
       header2.setTextSize(14);
       headerRow.addView(header2);

       TextView header3 = new TextView(PayMethods.this);
       header3.setGravity(Gravity.CENTER);

       header3.setText("Total");
       header3.setTextColor(getResources().getColor(R.color.text_color));
       header3.setLayoutParams(lp2);
       header3.setTextSize(14);
       headerRow.addView(header3);
       tabLayout.addView(headerRow);

       for (int j = 0; j < OrderTransactionsObj.size(); j++) {

           final TableRow row = new TableRow(PayMethods.this);


           for (int i = 0; i < 3; i++) {
                        TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
                        lp.setMargins(0, 10, 0, 0);
                        row.setLayoutParams(lp);
                        TextView textView = new TextView(PayMethods.this);
                        textView = new TextView(PayMethods.this);
                        textView.setGravity(Gravity.CENTER);
                        textView.setTextSize(10);
                        textView.setTextColor(getResources().getColor(R.color.text_color));
                        if(i==0) {
                            textView.setText("" + OrderTransactionsObj.get(j).getItemName());
                            textView.setLayoutParams(lp2);
                        }
                        if(i==1) {
                            textView.setText("" + OrderTransactionsObj.get(j).getQty());
                            textView.setLayoutParams(lp2);
                        }
                        if(i==2) {
                            textView.setText("" + OrderTransactionsObj.get(j).getTotal());
                            textView.setLayoutParams(lp2);
                        }


                        row.addView(textView);


                 }

        tabLayout.addView(row);
           total_money.setText(OrderHeaderObj.getAmountDue()+"");
           Log.e("total money",""+OrderHeaderObj.getAmountDue());
        }

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PrintHelper photoPrinter = new PrintHelper(PayMethods.this);
                photoPrinter.setScaleMode(PrintHelper.SCALE_MODE_FIT);
                linearLayout.setDrawingCacheEnabled(true);
                Bitmap bitmap = linearLayout.getDrawingCache();
                photoPrinter.printBitmap("invoice2.jpg", bitmap);

                Intent intent = new Intent(PayMethods.this, Order.class);
                startActivity(intent);
            }
        });
        dialog.show();

            }


    void initialize() {

        print = (ImageView) findViewById(R.id.print);

        cash = (Button) findViewById(R.id.cash);
        creditCard = (Button) findViewById(R.id.credit_card);
        cheque = (Button) findViewById(R.id.cheque);
        giftCard = (Button) findViewById(R.id.gift_card);
        credit = (Button) findViewById(R.id.credit);
        point = (Button) findViewById(R.id.point);
        save = (Button) findViewById(R.id.save);

        tableNumber = (TextView) findViewById(R.id.tableNumber);
        check = (TextView) findViewById(R.id.checkNumber);
        date = (TextView) findViewById(R.id.date);
        remainingBalance = (TextView) findViewById(R.id.remaining);
        server = (TextView) findViewById(R.id.server);
        orderAmount = (TextView) findViewById(R.id.order_amount);
        discount = (TextView) findViewById(R.id.discount);
        subCharge = (TextView) findViewById(R.id.sub_charge);
        subTotal = (TextView) findViewById(R.id.sub_total);
        tax = (TextView) findViewById(R.id.tax);
        amountDue = (TextView) findViewById(R.id.amount_due);
        deliveryCharge = (TextView) findViewById(R.id.delivery_charge);
        totalDue = (TextView) findViewById(R.id.total_due);
        totalReceived = (TextView) findViewById(R.id.total_received);
        balance = (TextView) findViewById(R.id.balance);

        cash.setOnClickListener(onClickListener);
        creditCard.setOnClickListener(onClickListener);
        cheque.setOnClickListener(onClickListener);
        giftCard.setOnClickListener(onClickListener);
        credit.setOnClickListener(onClickListener);
        point.setOnClickListener(onClickListener);
        save.setOnClickListener(onClickListener);

    }
}
