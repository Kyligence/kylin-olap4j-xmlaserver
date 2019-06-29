/*
 *
 *  * Copyright (C) 2016 Kyligence Inc. All rights reserved.
 *  *
 *  * http://kyligence.io
 *  *
 *  * This software is the confidential and proprietary information of
 *  * Kyligence Inc. ("Confidential Information"). You shall not disclose
 *  * such Confidential Information and shall use it only in accordance
 *  * with the terms of the license agreement you entered into with
 *  * Kyligence Inc.
 *  *
 *  * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 *  * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 *  * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 *  * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
 *  * OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 *  * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 *  * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 *  * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 *  * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 *  * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 *  * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 */
package mondrian.xmla;

import java.util.*;

import com.alibaba.ttl.TransmittableThreadLocal;

public class XmlaRequestContext {

    public static TransmittableThreadLocal<XmlaRequestContext> localContext = new TransmittableThreadLocal<XmlaRequestContext>();

    public XmlaRequestContext() {
        localContext.set(this);
    }

    public void clear() {
        localContext.remove();
    }

    public Map<String, String> paramsMap;

    public void setParameter(String name, String value) {
        if (paramsMap == null) {
            paramsMap = new HashMap<String, String>(8);
        }
        paramsMap.put(name, value);
    }

    public String getParameter(String name) {
        return paramsMap == null ? null : paramsMap.get(name);
    }

    public String currentCatalog;

    public String currentUser;

    public String currentProject;

    public boolean hasRefreshedSchema;

    public String mdxQuery;

    public String clientType;

    public QueryPage queryPage;

    public RunningStatistics runningStatistics;

    public MdxOptimizer mdxOptimizer;

    public static class QueryPage {
        public int queryStart;

        public int queryEnd;

        public int pageSize;

        public int startPage;

        public int endPage;

        public int pageStart;

        public int pageEnd;

        public boolean inOnePage;
    }

    public class RunningStatistics {

        public long calcAxesTime;
        public long calcCellValueTime;
        public long createRolapResultTime;
        public long unparseMultiDimDatasetTime;
        public long marshallSoapMessageTime;
        public List<Long> fetchSqlTotalTime = new ArrayList<Long>(4);
        public long mdxRunTotalTime;

        public String getReportString() {
            if (mdxQuery == null || mdxQuery.length() == 0) {
                return null;
            }
            String ls = System.getProperty("line.separator");
            StringBuffer sb = new StringBuffer();
            sb.append(ls);
            sb.append("********************* MDX Running Statistics **********************").append(ls);
            sb.append("total time: ").append(mdxRunTotalTime).append("ms").append(ls);
            int sqlExeCount = fetchSqlTotalTime.size();
            if (sqlExeCount == 0) {
                sb.append("SQL execute : 0 sql executed").append(ls);
            } else {
                sb.append("SQL execute : ").append(sqlExeCount).append(" sql executed, respectively, ");
                for (int i = 0; i < sqlExeCount; i++) {
                    sb.append(fetchSqlTotalTime.get(i)).append("ms").append(", ");
                }
                sb.delete(sb.length() - 2, sb.length());
                sb.append(ls);
            }
            sb.append("calculate axes : ").append(calcAxesTime).append("ms").append(ls);
            sb.append("calculate cell : ").append(calcCellValueTime).append("ms").append(ls);
            sb.append("create RolapResult : ").append(createRolapResultTime).append("ms").append(ls);
            sb.append("create MultiDimensional Dataset: ").append(unparseMultiDimDatasetTime).append("ms").append(ls);
            sb.append("marshall soap message: ").append(marshallSoapMessageTime).append("ms").append(ls);
            sb.append("********************************************************************");
            return sb.toString();
        }

    }

    public interface Parameter {

        String ENABLE_OPTIMIZE_MDX = "enableOptimizeMDX";

        String NEED_CALCULATE_TOTAL = "needCalculateTotal";
    }

    public interface ClientType {

        String SMARTBI = "SmartBI";

        String EXCEL = "Excel";

        String XMLA_CONNECT = "Xmla-Connect";
    }

}
