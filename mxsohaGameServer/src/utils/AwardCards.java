package utils;

/**
 * Created by Administrator on 2018/1/3.
 * TYPE_DOUBLE 1 一队牌型*
 * 
 * TYPE_TOW_DOUBLE 2 两队牌型*
 * 
 * TYPE_THIRD 3 三张一样的牌型*
 *  
 * TYPE_STR 4 顺子牌型 
 * TYPE_T_D 6 葫芦牌型*
 * TYPE_SAME 5 同花牌型 *
 * TYPE_FOUR 7 四条牌型*
 * 
 * TYPE_SAME_STR 8 同花顺牌型 *
 * TYPE_SAME_STR_BIG 9 同花大顺牌型 *
 * 
 * TYPE_FIVE 10 五条牌型*
 * 
 * MinDouble 9 最小可奖励的对子
 */

public class AwardCards
{
    /**
     * 一对
     */
    public static final int TYPE_DOUBLE=1;
    /**
     * 两对
     */
    public static final int TYPE_TOW_DOUBLE=2;
    /**
     * 三张
     */
    public static final int TYPE_THIRD=3;
    /**
     * 顺子
     */
    public static final int TYPE_STR=4;
    /**
     * 同花
     */
    public static final int TYPE_SAME=5;
    /**
     * 葫芦
     */
    public static final int TYPE_T_D=6;
    /**
     * 4条
     */
    public static final int TYPE_FOUR=7;
    /**
     * 同花顺
     */
    public static final int TYPE_SAME_STR = 8;
    /**
     * 同花大顺
     */
    public static final int TYPE_SAME_STR_BIG=9;
    /**
     * 五条
     */
    public static final int TYPE_FIVE = 10;

    /**
     * 最小可奖励对的值（小于10不奖励）
     */
    public static final int MinDouble = 9;
    
    
    /**
     * 获取一组牌的牌型
     * @param indexes   扑克牌数组（0—56）
     * @return  牌型
     */
    public static int award(int[] indexes)
    {
        int i=0;
        int j=0;
        int temp=0;
        /**
         * 当前牌型
         */
        int type=0;
        /**
         * 成对牌的值
         */
        int doubleValue=0;
        /**
         * 王的数量
         */
        int kings=0;
        /**
         * 相同牌的数量
         */
        int sameNum=0;
        /**
         * 存放值得数组
         */
        int[] values;
        /**
         * 相同状态
         */
        boolean same=true;
        /**
         * 连续状态
         */
        boolean conti=true;

        MyCard[] cards=new MyCard[indexes.length];

        for(i=0;i<indexes.length;i++)                           //实例化扑克对象
        {
            cards[i]=new MyCard(indexes[i]);
            if(indexes[i]>=52)                                   //记录王的数量
            {
                kings++;
            }
        }

        values=new int[indexes.length-kings];
        temp=0;
        for(i=0;i<cards.length;i++)                             //将非王的牌的值存起来
        {
            if(cards[i].index<52)
            {
                values[temp]=(cards[i].getCardValu()==0?13:cards[i].getCardValu());
                temp++;
            }
        }

        for(i=0;i< values.length;i++)                           //排序
        {
            for(j=i;j<values.length;j++)
            {
                if(values[i]<values[j])
                {
                    temp=values[i];
                    values[i]=values[j];
                    values[j]=temp;
                }
            }
        }

        for(i=1;i<values.length;i++)                            //开始判断牌型
        {
            if(values[i]==values[i-1])                          //和前一张牌相同
            {
                sameNum++;
                conti=false;
            }
            else                                                //和前一张牌不同
            {
                if(sameNum>0)                                            //此前的牌是否相同
                {
                    temp=sameNumToType(sameNum);
                    if(temp==TYPE_DOUBLE&&type==0)
                    {
                        doubleValue=values[i-1];
                    }
                    type=changeType(type,temp);
                }
                else
                {
                    if(conti&&(values[i]==(values[i-1]-1)))             //牌型连续
                    {

                    }
                    else                                                //牌型未连续
                    {
                        if(i==1&&values[0]==13&&values[values.length-1]==1)//对12345小顺进行特殊判断
                        {

                        }
                        else
                        {
                            conti=false;
                        }
                    }
                }
                sameNum=0;
            }
        }

        if(sameNum>0)                                                   //最后一张牌是否和此前的牌相同，循环中未处理因此在结束循环后单独处理一次
        {
            temp=sameNumToType(sameNum);
            if(temp==TYPE_DOUBLE&&type==0)
            {
                doubleValue=values[values.length-1];
            }
            type=changeType(type,temp);
        }
        temp=-1;

        for(i=0;i<cards.length;i++)
        {
            if(temp==-1&&cards[i].index<52)
            {
                temp=cards[i].cardType;
            }
            if(cards[i].cardType!=temp&&cards[i].index<52&&temp!=-1)//判断同花
            {
                same=false;
                break;
            }
        }

        if(conti)
        {
            if(same)
            {
                if(values[0]==13&&values[1]==12)
                {
                    type=TYPE_SAME_STR_BIG;                        //同花大顺
                }
                else
                {
                    type=TYPE_SAME_STR;                             //同花顺
                }
            }
            else
            {
                type=TYPE_STR;                                      //顺子
            }
        }
        else
        {
            if(same)
            {
                type=TYPE_SAME;                                     //同花
            }
            else
            {

            }
        }

        doubleValue=(type!=TYPE_DOUBLE)?13:doubleValue;
        if(kings>0)
        {
            if(kings<4)
            {
                type=changeTypeWithKing(type,kings,values);
            }
            else
            {
                type=TYPE_FIVE;
            }
        }

        if(type==TYPE_DOUBLE){if(doubleValue<MinDouble){type=0;}}//最终1对牌型小于MinDouble值则算是散牌

        return type;
    }

    /**
     * 结合王的数量判断牌型
     * @param type
     * @param kings
     * @param values
     * @return
     */
    private  static  int changeTypeWithKing(int type,int kings,int[] values)
    {
        int temp= 0;
        switch(type)
        {
            case TYPE_FOUR://4张
            {
                return TYPE_FIVE;
            }
            case TYPE_SAME_STR_BIG://同花大顺
            {
                return TYPE_SAME_STR_BIG;
            }
            case TYPE_SAME_STR://同花顺
            {
                if (values[0]+kings>=13)//同花大顺
                {
                    return TYPE_SAME_STR_BIG;
                }
                else
                {
                    return TYPE_SAME_STR;
                }
            }
            case TYPE_SAME:
            {
                temp=values[0]-values[values.length-1];
                if (temp<5)//同花顺
                {
                    if (kings-(temp-1)+values[0]>=13)//同花大顺
                    {
                        return TYPE_SAME_STR_BIG;
                    }
                    else//同花顺
                    {
                        return TYPE_SAME_STR;
                    }
                }
                else if(values[0]==13&&values[1]==4)            //同花小顺
                {
                    return  TYPE_SAME_STR;
                }
                else//同花
                {
                    if (kings==3)//三张王
                    {
                        return TYPE_FOUR;
                    }
                    else
                    {
                        return TYPE_SAME;
                    }

                }
            }
            case TYPE_STR:
            {
                if (kings==3)//三张王
                {
                    return TYPE_FOUR;
                }
                else
                {
                    return TYPE_STR;
                }
            }
            case TYPE_THIRD:
            {
                if (kings==2)
                {
                    return TYPE_FIVE;
                }
                else
                {
                    return TYPE_FOUR;
                }
            }
            case TYPE_TOW_DOUBLE:
            {
                return TYPE_T_D;
            }
            case TYPE_DOUBLE:
            {
                switch(kings)
                {
                    case 1:
                    {
                        return TYPE_THIRD;
                    }
                    case 2:
                    {
                        return TYPE_FOUR;
                    }
                    case 3:
                    {
                        return TYPE_FIVE;
                    }
                    default:
                    {
                        return type;
                    }
                }
            }
            case 0://散牌
            {
                if (kings<3)//三张以下王顺子最大，其次是三张，再次为一对
                {
                    if (values[0]-values[values.length-1]<5)//顺子
                    {
                        return TYPE_STR;
                    }
                    else if(values[0]==13&&values[1]==4)        //A-5小顺
                    {
                        return  TYPE_STR;
                    }
                    else
                    {
                        if (kings==2)
                        {
                            return TYPE_THIRD;
                        }
                        else
                        {
                            if (values[0]>=MinDouble)
                            {
                                return TYPE_DOUBLE;
                            }
                            else
                            {
                                return 0;
                            }

                        }
                    }
                }
                else//三张王，最大为4k
                {
                    return TYPE_FOUR;
                }
            }

        }

        return type;
    }

    /**
     * 根据已有牌型以及增加的牌型计算综合牌型，如已有牌型“一对”新增牌型“一对”则综合牌型为“两对”
     * @param type1 当前牌型0是没有牌型
     * @param newType   新增牌型
     * @return  综合牌型
     */
    private static  int changeType(int type1,int newType)
    {
        if(type1==0)//如果当前没有牌型，则牌型反馈为新增牌型
        {
            return newType;
        }
        else if(type1==TYPE_DOUBLE)
        {
            if(newType==TYPE_THIRD)//葫芦
            {
                return TYPE_T_D;
            }
            else if(newType==TYPE_DOUBLE)//两队
            {
                return TYPE_TOW_DOUBLE;
            }
        }
        else if(type1==TYPE_THIRD)
        {
            if(newType==TYPE_DOUBLE)//葫芦
            {
                return TYPE_T_D;
            }
        }

        return type1;
    }

    /**
     * 根据相同牌的数量判断牌型
     * @param sameNum
     * @return
     */
    private static int sameNumToType(int sameNum)
    {
        switch(sameNum)
        {
            case 1://一对
            {
                return TYPE_DOUBLE;
            }
            case 2://三张
            {
                return TYPE_THIRD;
            }
            case 3://四张
            {
                return TYPE_FOUR;
            }
            case 4://五张
            {
                return TYPE_FIVE;
            }

        }

        return -1;
    }



    static class MyCard
    {
        /**
         * 扑克牌索引值
         */
        private int index=0;
        /**
         * 扑克牌花色
         */
        private int cardType=0;
        /**
         * 扑克牌大小
         */
        private int cardValu=0;
        public MyCard(int ind)
        {
            index=(int)ind;
            cardType= (int) Math.floor(ind/13);
            cardValu=index%13;
        }

        /**
         * 扑克牌索引值
         */
        public int getIndex()
        {
            return index;
        }
        /**
         * 扑克牌花色
         */
        public int getCardType()
        {
            return cardType;
        }
        /**
         * 扑克牌大小
         */
        public int getCardValu()
        {
            return cardValu;
        }
    }
}


