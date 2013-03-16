package com.sssta.daomeidan;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import android.R.integer;

public class core_code
{

	public Person rand (List<Person> personlist) 
	{
	//	int max = 0;
		Person max_num_person = new Person();
		for (int i = 0; i < personlist.size(); i++)
		{
			int num = (int)(Math.random()*100+Math.random()*10);
			personlist.get(i).setNum(num);
			for(int k = 0;k<i; k++)
			{
				while(true)
				{
					if(personlist.get(i).getNum() == personlist.get(k).getNum() && k != i)
					{
						num = (int)(Math.random()*100+Math.random()*10);
						personlist.get(i).setNum(num);
						k = 0;
					}
					else break;
				}
			}
		}
		for(int i = 0;i<personlist.size();i++)
		{
			if(max_num_person.getNum()<personlist.get(i).getNum())
				max_num_person = personlist.get(i);
		}
		return max_num_person;
	}
}
