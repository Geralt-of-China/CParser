
int main()
{
    //FILE *fp;                      /*����һ��ָ��FILE���ͽṹ���ָ�����*/
    char ch, filename[50];                /*�������������Ϊ�ַ���*/
    printf("please input file`s name;\n");
    gets (filename);                 /*�����ļ�����·��������*/
    fp = fopen(filename, "r");           /*��ֻ����ʽ��ָ���ļ�*/
    ch = fgetc(fp);                   /*fgetc��������һ���ַ�����ch*/
    while (ch != EOF)                    /*��������ַ�ֵ����EOFʱ����ѭ��*/
    {
        putchar(ch);               /*��������ַ��������Ļ��*/
        ch = fgetc(fp);               /*fgetc������������һ���ַ�����ch*/
    }
    fclose (fp);                    /*�ر��ļ�*/
}
/*������д���ļ�*/
int main()
{
    //FILE *fp;                                  /*����һ��ָ��FILE���ͽṹ���ָ�����*/
    char ch, filename[50];                            /*�������������Ϊ�ַ���*/
    printf("please input filename:\n");
    scanf("%s", filename);                         /*�����ļ�����·��������*/
    if ((fp = fopen(filename, "w")) == NULL)              /*��ֻд��ʽ��ָ���ļ�*/
    {
        printf("cannot open file\n");
        exit(0);
    }
    ch = getchar();                               /*fgetc��������һ���ַ�����ch*/
    while (ch != '#')                                /*������"#"ʱ����ѭ��*/
    {
        fputc(ch, fp);                             /*��������ַ�д�������ļ���ȥ*/
        ch = getchar();                           /*fgetc������������һ���ַ�����ch*/
    }
    fclose(fp);                                /*�ر��ļ�*/
}