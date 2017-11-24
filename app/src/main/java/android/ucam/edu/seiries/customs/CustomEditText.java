package android.ucam.edu.seiries.customs;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v7.widget.AppCompatEditText;
import android.util.AttributeSet;


public class CustomEditText extends AppCompatEditText {
    private Paint p1;
    private Paint p2;
    private float escala;

    public CustomEditText(Context context, AttributeSet attrs, int defStyle){
        super(context, attrs, defStyle);
        inicializacion();
    }

    public CustomEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        inicializacion();
    }

    public CustomEditText(Context context) {
        super(context);
        inicializacion();
    }

    private void inicializacion()
    {
        p1 = new Paint(Paint.ANTI_ALIAS_FLAG);
        p1.setColor(Color.WHITE);
        p1.setStyle(Paint.Style.FILL);

        p2 = new Paint(Paint.ANTI_ALIAS_FLAG);
        p2.setColor(Color.BLACK);
        p2.setTextSize(20);
        escala = getResources().getDisplayMetrics().density;
    }

    @Override
    public void onDraw(Canvas canvas)
    {
        //Llamamos al método de la clase base (EditText)
        super.onDraw(canvas);
        //Dibujamos el fondo negro del contador
        canvas.drawRect(this.getWidth()-30*escala,5*escala,this.getWidth()-5*escala,20*escala, p1) ;

        //Dibujamos el número de caracteres sobre el contador
        canvas.drawText("" + this.getText().toString().length(),this.getWidth()-28*escala,17*escala, p2);
    }
}
