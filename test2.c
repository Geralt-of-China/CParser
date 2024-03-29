
int main()
{
    //FILE *fp;                      /*定义一个指向FILE类型结构体的指针变量*/
    char ch, filename[50];                /*定义变量及数组为字符型*/
    printf("please input file`s name;\n");
    gets (filename);                 /*输入文件所在路径及名称*/
    fp = fopen(filename, "r");           /*以只读方式打开指定文件*/
    ch = fgetc(fp);                   /*fgetc函数带回一个字符赋给ch*/
    while (ch != EOF)                    /*当读入的字符值等于EOF时结束循环*/
    {
        putchar(ch);               /*将读入的字符输出在屏幕上*/
        ch = fgetc(fp);               /*fgetc函数继续带回一个字符赋给ch*/
    }
    fclose (fp);                    /*关闭文件*/
}
/*将内容写入文件*/
int main()
{
    //FILE *fp;                                  /*定义一个指向FILE类型结构体的指针变量*/
    char ch, filename[50];                            /*定义变量及数组为字符型*/
    printf("please input filename:\n");
    scanf("%s", filename);                         /*输入文件所在路径及名称*/
    if ((fp = fopen(filename, "w")) == NULL)              /*以只写方式打开指定文件*/
    {
        printf("cannot open file\n");
        exit(0);
    }
    ch = getchar();                               /*fgetc函数带回一个字符赋给ch*/
    while (ch != '#')                                /*当输入"#"时结束循环*/
    {
        fputc(ch, fp);                             /*将读入的字符写到磁盘文件上去*/
        ch = getchar();                           /*fgetc函数继续带回一个字符赋给ch*/
    }
    fclose(fp);                                /*关闭文件*/
}