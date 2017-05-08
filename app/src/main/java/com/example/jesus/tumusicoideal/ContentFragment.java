package com.example.jesus.tumusicoideal;

import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class ContentFragment extends Fragment{
    public static final String ARG_SECTION_TITLE = "section_title";

    public static ContentFragment newInstance(String sectionTitle) {
        ContentFragment fragment = new ContentFragment();

        // Establecemos el titulo enviado como argumento del fragment
        Bundle args = new Bundle();
        args.putString(ARG_SECTION_TITLE, sectionTitle);
        fragment.setArguments(args);
        return fragment;
    }


    public ContentFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.content_main, container, false);

        // Mostramos el titulo que guardamos como argumento del fragment de la sección en el TextView
        String title = getArguments().getString(ARG_SECTION_TITLE);
        TextView textView = (TextView) view.findViewById(R.id.text_view);
        textView.setText(title);
        return view;
    }
}
