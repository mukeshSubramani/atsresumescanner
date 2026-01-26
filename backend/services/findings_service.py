"""
Findings service - generates resume findings and suggestions
"""
from typing import Dict, List, Any
import re

class FindingsService:
    """Service for generating resume findings and suggestions"""
    
    # Common resume sections
    COMMON_SECTIONS = ['experience', 'skills', 'education']
    
    # Technical skills database - core programming languages, frameworks, tools, databases, cloud, etc.
    TECHNICAL_SKILLS = {
        # Programming Languages
        'python', 'java', 'javascript', 'typescript', 'c', 'cpp', 'c++', 'csharp', 'c#',
        'go', 'golang', 'rust', 'ruby', 'php', 'swift', 'kotlin', 'scala', 'r', 'perl',
        'matlab', 'julia', 'dart', 'elixir', 'haskell', 'lua', 'groovy', 'clojure',
        'objective-c', 'objectivec', 'bash', 'shell', 'powershell', 'vba',
        
        # Web Technologies
        'html', 'html5', 'css', 'css3', 'react', 'reactjs', 'angular', 'angularjs',
        'vue', 'vuejs', 'svelte', 'nextjs', 'next.js', 'nuxtjs', 'nuxt.js', 'webpack',
        'vite', 'tailwind', 'tailwindcss', 'bootstrap', 'jquery', 'sass', 'scss', 'less',
        'redux', 'mobx', 'rxjs', 'graphql', 'apollo', 'relay', 'material-ui', 'mui',
        'chakra', 'ant-design', 'styled-components', 'emotion',
        
        # Backend Frameworks
        'django', 'flask', 'fastapi', 'express', 'expressjs', 'nodejs', 'node.js',
        'spring', 'springboot', 'spring-boot', 'rails', 'laravel', 'symfony', 'aspnet',
        'asp.net', 'dotnet', '.net', 'nestjs', 'nest.js', 'koa', 'hapi', 'sinatra',
        'gin', 'echo', 'fiber', 'actix', 'rocket', 'axum',
        
        # Databases
        'sql', 'mysql', 'postgresql', 'postgres', 'mongodb', 'mongo', 'redis',
        'elasticsearch', 'elastic', 'dynamodb', 'oracle', 'cassandra', 'neo4j',
        'sqlite', 'mariadb', 'mssql', 'sqlserver', 'couchdb', 'firebase', 'firestore',
        'influxdb', 'timescaledb', 'cockroachdb', 'memcached', 'etcd',
        
        # Cloud & Infrastructure
        'aws', 'azure', 'gcp', 'google-cloud', 'docker', 'kubernetes', 'k8s',
        'jenkins', 'terraform', 'ansible', 'puppet', 'chef', 'vagrant', 'packer',
        'cloudformation', 'pulumi', 'helm', 'istio', 'consul', 'vault', 'nomad',
        'openshift', 'rancher', 'ecs', 'eks', 'aks', 'gke', 'lambda', 'fargate',
        's3', 'ec2', 'rds', 'cloudfront', 'route53', 'vpc', 'iam',
        
        # CI/CD & DevOps
        'gitlab', 'github', 'bitbucket', 'circleci', 'travis', 'github-actions',
        'gitlab-ci', 'teamcity', 'bamboo', 'octopus', 'spinnaker', 'argo', 'argocd',
        'flux', 'tekton', 'drone', 'buildkite',
        
        # Data Science & ML
        'pandas', 'numpy', 'scipy', 'matplotlib', 'seaborn', 'plotly', 'tensorflow',
        'pytorch', 'keras', 'scikit-learn', 'sklearn', 'spark', 'pyspark', 'hadoop',
        'airflow', 'jupyter', 'notebook', 'anaconda', 'conda', 'mlflow', 'kubeflow',
        'sagemaker', 'databricks', 'snowflake', 'bigquery', 'redshift', 'athena',
        'hive', 'presto', 'trino', 'dbt', 'looker', 'tableau', 'powerbi', 'metabase',
        'xgboost', 'lightgbm', 'catboost', 'opencv', 'nltk', 'spacy', 'gensim',
        'transformers', 'huggingface', 'langchain', 'ml', 'ai',
        
        # Testing
        'junit', 'pytest', 'unittest', 'jest', 'mocha', 'chai', 'jasmine', 'karma',
        'cypress', 'selenium', 'webdriver', 'puppeteer', 'playwright', 'testng',
        'cucumber', 'behave', 'rspec', 'minitest', 'phpunit', 'xunit', 'nunit',
        'testcafe', 'detox', 'appium', 'postman', 'insomnia', 'jmeter', 'gatling',
        'locust', 'k6',
        
        # Version Control
        'git', 'svn', 'mercurial', 'perforce', 'cvs',
        
        # Mobile Development
        'ios', 'android', 'react-native', 'reactnative', 'flutter', 'xamarin',
        'ionic', 'cordova', 'phonegap', 'swiftui', 'jetpack', 'compose',
        
        # Message Queues & Streaming
        'kafka', 'rabbitmq', 'activemq', 'zeromq', 'nats', 'pulsar', 'kinesis',
        'sqs', 'sns', 'pubsub', 'eventbridge', 'redis-streams',
        
        # Monitoring & Logging
        'prometheus', 'grafana', 'datadog', 'newrelic', 'splunk', 'elk', 'logstash',
        'kibana', 'fluentd', 'loki', 'tempo', 'jaeger', 'zipkin', 'sentry',
        'cloudwatch', 'stackdriver', 'appinsights',
        
        # Web Servers & Reverse Proxy
        'nginx', 'apache', 'httpd', 'tomcat', 'iis', 'caddy', 'traefik', 'haproxy',
        'envoy', 'kong',
        
        # APIs & Protocols
        'rest', 'restful', 'soap', 'grpc', 'websocket', 'graphql', 'oauth', 'oauth2',
        'jwt', 'saml', 'openid', 'cors', 'jsonrpc', 'xmlrpc', 'protobuf', 'thrift',
        'avro', 'messagepack', 'api', 'sdk',
        
        # Architectures & Patterns
        'microservices', 'serverless', 'monolith', 'soa', 'event-driven', 'cqrs',
        'saga', 'api-gateway', 'service-mesh',
        
        # Methodologies
        'agile', 'scrum', 'kanban', 'lean', 'devops', 'sre', 'tdd', 'bdd', 'ddd',
        'ci', 'cd', 'cicd', 'gitops', 'devsecops',
        
        # Operating Systems & Tools
        'linux', 'unix', 'ubuntu', 'debian', 'centos', 'rhel', 'fedora', 'alpine',
        'windows', 'macos', 'vim', 'emacs', 'vscode', 'intellij', 'pycharm',
        'webstorm', 'eclipse', 'netbeans', 'xcode', 'androidstudio',
        
        # Other Technologies
        'blockchain', 'ethereum', 'solidity', 'web3', 'cryptocurrency', 'bitcoin',
        'smart-contracts', 'nft', 'defi', 'ipfs', 'wasm', 'webassembly', 'webgl',
        'three.js', 'threejs', 'd3', 'd3.js', 'chart.js', 'highcharts',
    }
    
    # Company names and brands to exclude (not skills)
    COMPANY_BRANDS = {
        'google', 'facebook', 'meta', 'amazon', 'microsoft', 'apple', 'netflix',
        'spotify', 'uber', 'lyft', 'airbnb', 'twitter', 'linkedin', 'instagram',
        'snapchat', 'tiktok', 'tesla', 'spacex', 'nvidia', 'intel', 'amd',
        'salesforce', 'oracle', 'ibm', 'sap', 'adobe', 'slack', 'zoom',
        'dropbox', 'box', 'atlassian', 'jira', 'confluence', 'notion',
        'asana', 'trello', 'monday', 'clickup', 'basecamp', 'stripe',
        'paypal', 'square', 'shopify', 'wix', 'squarespace', 'mailchimp',
        'hubspot', 'zendesk', 'intercom', 'twilio', 'sendgrid', 'cloudflare',
        'fandom', 'reddit', 'pinterest', 'tumblr', 'quora', 'medium',
        'stackoverflow', 'yahoo', 'bing', 'duckduckgo', 'wikipedia',
        'mozilla', 'opera', 'brave', 'discord', 'telegram', 'whatsapp',
    }
    
    # Common adjectives and non-skill words to exclude
    ADJECTIVES_AND_VERBS = {
        # Adjectives
        'strong', 'excellent', 'outstanding', 'good', 'great', 'best', 'top',
        'senior', 'junior', 'mid', 'lead', 'principal', 'staff', 'expert',
        'experienced', 'skilled', 'talented', 'proficient', 'advanced', 'basic',
        'intermediate', 'beginner', 'proven', 'demonstrated', 'successful',
        'effective', 'efficient', 'reliable', 'responsible', 'dedicated',
        'motivated', 'passionate', 'enthusiastic', 'creative', 'innovative',
        'dynamic', 'flexible', 'adaptable', 'collaborative', 'independent',
        'detail-oriented', 'analytical', 'strategic', 'tactical', 'technical',
        'non-technical', 'functional', 'cross-functional', 'full-stack',
        'fullstack', 'front-end', 'frontend', 'back-end', 'backend',
        
        # Nouns (roles, generic terms)
        'engineer', 'engineers', 'developer', 'developers', 'programmer',
        'architect', 'manager', 'lead', 'director', 'analyst', 'scientist',
        'designer', 'consultant', 'specialist', 'intern', 'coordinator',
        'administrator', 'technician', 'operator', 'representative',
        'team', 'teams', 'member', 'members', 'colleague', 'colleagues',
        'client', 'clients', 'customer', 'customers', 'user', 'users',
        'stakeholder', 'stakeholders', 'partner', 'partners', 'vendor',
        'company', 'organization', 'department', 'division', 'group',
        'project', 'projects', 'product', 'products', 'service', 'services',
        'solution', 'solutions', 'system', 'systems', 'platform', 'platforms',
        'application', 'applications', 'software', 'hardware', 'infrastructure',
        'environment', 'environments', 'process', 'processes', 'workflow',
        'pipeline', 'pipelines', 'tool', 'tools', 'technology', 'technologies',
        'framework', 'frameworks', 'library', 'libraries', 'module', 'modules',
        'component', 'components', 'feature', 'features', 'functionality',
        'requirement', 'requirements', 'specification', 'specifications',
        'documentation', 'document', 'documents', 'report', 'reports',
        'meeting', 'meetings', 'discussion', 'discussions', 'review', 'reviews',
        
        # Generic verbs and words
        'new', 'existing', 'current', 'previous', 'next', 'last', 'first',
        'second', 'third', 'multiple', 'various', 'several', 'many', 'few',
        'more', 'less', 'most', 'least', 'all', 'some', 'any', 'each',
        'other', 'another', 'different', 'same', 'similar', 'related',
        'work', 'working', 'worked', 'works', 'build', 'building', 'built',
        'develop', 'developing', 'developed', 'design', 'designing', 'designed',
        'create', 'creating', 'created', 'implement', 'implementing', 'implemented',
        'maintain', 'maintaining', 'maintained', 'support', 'supporting', 'supported',
        'improve', 'improving', 'improved', 'optimize', 'optimizing', 'optimized',
        'enhance', 'enhancing', 'enhanced', 'update', 'updating', 'updated',
        'upgrade', 'upgrading', 'upgraded', 'migrate', 'migrating', 'migrated',
        'deploy', 'deploying', 'deployed', 'manage', 'managing', 'managed',
        'coordinate', 'coordinating', 'coordinated', 'collaborate', 'collaborating',
        'communicate', 'communicating', 'ensure', 'ensuring', 'ensured',
        'provide', 'providing', 'provided', 'deliver', 'delivering', 'delivered',
        'achieve', 'achieving', 'achieved', 'complete', 'completing', 'completed',
        'perform', 'performing', 'performed', 'conduct', 'conducting', 'conducted',
        'execute', 'executing', 'executed', 'plan', 'planning', 'planned',
        'analyze', 'analyzing', 'analyzed', 'evaluate', 'evaluating', 'evaluated',
        'assess', 'assessing', 'assessed', 'review', 'reviewing', 'reviewed',
        'test', 'testing', 'tested', 'debug', 'debugging', 'debugged',
        'troubleshoot', 'troubleshooting', 'troubleshot', 'resolve', 'resolving',
        'power', 'powers', 'flexibility', 'scale', 'scaling', 'growth',
        'impact', 'value', 'quality', 'performance', 'speed', 'efficiency',
        'productivity', 'reliability', 'availability', 'security', 'privacy',
        'compliance', 'governance', 'ownership', 'accountability', 'responsibility',
        'leadership', 'mentorship', 'guidance', 'training', 'learning',
        'knowledge', 'understanding', 'expertise', 'experience', 'background',
        'education', 'degree', 'certification', 'award', 'recognition',
        'ability', 'abilities', 'skill', 'skills', 'capability', 'capabilities',
        'closely', 'internal', 'external', 'remote', 'onsite', 'hybrid',
        'full-time', 'part-time', 'contract', 'freelance', 'permanent',
        'temporary', 'seasonal', 'hourly', 'salary', 'benefits',
    }
    
    def generate_findings(
        self,
        resume_content: str,
        missing_keywords: List[str],
        word_count: int
    ) -> Dict[str, Any]:
        """
        Generate findings for the resume
        
        Args:
            resume_content: Original resume text
            missing_keywords: List of missing keywords
            word_count: Number of words in resume
            
        Returns:
            Dictionary with missingSkills, formattingWarnings, and suggestions
        """
        missing_skills = self._extract_technical_skills(missing_keywords)
        formatting_warnings = self._check_formatting(resume_content, word_count)
        suggestions = self._generate_suggestions()
        
        return {
            "missingSkills": missing_skills,
            "formattingWarnings": formatting_warnings,
            "suggestions": suggestions
        }
    
    def _extract_technical_skills(self, missing_keywords: List[str]) -> List[str]:
        """
        Extract technical skills from missing keywords using hybrid approach
        
        Filters out:
        - Company names and brands
        - Common adjectives and non-skill words
        
        Includes:
        - Keywords from technical skills database
        - Keywords matching technical patterns (version numbers, acronyms, etc.)
        """
        skills = []
        for keyword in missing_keywords:
            # Normalize keyword for comparison
            keyword_lower = keyword.lower()
            
            # First check if it's in our technical skills database (handles short keywords like 'go', 'r', 'ci', 'cd')
            if keyword_lower in self.TECHNICAL_SKILLS:
                skills.append(keyword)
                continue
            
            # Skip if too short (after checking database)
            if len(keyword) <= 2:
                continue
            
            # Skip company names/brands
            if keyword_lower in self.COMPANY_BRANDS:
                continue
            
            # Skip common adjectives and non-skill words
            if keyword_lower in self.ADJECTIVES_AND_VERBS:
                continue
            
            # Include if it matches technical patterns
            if self._is_likely_technical(keyword):
                skills.append(keyword)
        
        return skills
    
    def _is_likely_technical(self, keyword: str) -> bool:
        """
        Check if a keyword is likely a technical term using pattern recognition
        
        Recognizes:
        - Version numbers (python3, java8, v2, 2.0)
        - Technical naming patterns (camelCase, snake_case, kebab-case)
        - Acronyms (AWS, API, CI/CD - 2-6 uppercase letters)
        - Technical suffixes (js, py, sql, db, api, ui, ux, ml, ai, ci, cd)
        - Technical prefixes (web-, micro-, cloud-, data-, dev-, ops-)
        - Special characters commonly used in tech (., -, _, +)
        """
        # Pattern 1: Contains digits (version numbers like python3, v2, 2.0)
        if any(c.isdigit() for c in keyword):
            return True
        
        # Pattern 2: Contains special technical characters
        if any(c in keyword for c in ['.', '-', '_', '+']):
            # But not if it's just punctuation
            if any(c.isalnum() for c in keyword):
                return True
        
        # Pattern 3: Acronyms (2-6 consecutive uppercase letters)
        if re.match(r'^[A-Z]{2,6}$', keyword):
            return True
        
        # Pattern 4: CamelCase pattern (indicates code/framework)
        if re.search(r'[a-z][A-Z]', keyword):
            return True
        
        # Pattern 5: Technical suffixes
        technical_suffixes = {
            'js', 'py', 'sql', 'db', 'api', 'ui', 'ux', 'ml', 'ai',
            'ci', 'cd', 'io', 'dev', 'ops', 'lib', 'sdk', 'ide',
            'cli', 'gui', 'css', 'xml', 'json', 'yaml', 'yml',
            'app', 'web', 'net', 'kit', 'framework', 'tech',
        }
        keyword_lower = keyword.lower()
        for suffix in technical_suffixes:
            if keyword_lower.endswith(suffix) and len(keyword) > len(suffix):
                return True
        
        # Pattern 6: Technical prefixes (more flexible matching)
        technical_prefixes = {
            'web', 'micro', 'cloud', 'data', 'dev', 'ops',
            'multi', 'cross', 'open', 'proto', 'meta', 'infra',
            'cyber', 'tech', 'digital',
        }
        for prefix in technical_prefixes:
            # Must have more than 2 characters after the prefix to avoid matching common words
            if keyword_lower.startswith(prefix) and len(keyword) > len(prefix) + 2:
                rest = keyword[len(prefix):]
                # Safety check: ensure rest is not empty
                if len(rest) > 0:
                    # Check if what comes after looks technical (has dash, underscore, uppercase, or specific tech words)
                    if rest[0] in ['-', '_'] or rest[0].isupper():
                        return True
                    # Also check for common technical compound words
                    if rest in ['server', 'service', 'services', 'native', 'stack', 'flow', 'shop']:
                        return True
        
        return False
    
    def _check_formatting(self, resume_text: str, word_count: int) -> List[str]:
        """Check for formatting issues"""
        warnings = []
        
        # Check length
        if word_count < 250:
            warnings.append("Resume is shorter than 250 words")
        elif word_count > 1200:
            warnings.append("Resume is longer than 1200 words")
        
        # Check for common sections
        resume_lower = resume_text.lower()
        missing_sections = []
        
        for section in self.COMMON_SECTIONS:
            if section not in resume_lower:
                missing_sections.append(section.capitalize())
        
        if missing_sections:
            warnings.append(f"Missing common sections: {', '.join(missing_sections)}")
        
        return warnings
    
    def _generate_suggestions(self) -> List[str]:
        """Generate improvement suggestions"""
        return [
            "Add missing keywords naturally in Skills and Experience sections",
            "Mirror important tools and technologies from the job description",
            "Ensure clear section headings for ATS readability"
        ]
